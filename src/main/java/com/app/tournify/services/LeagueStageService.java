package com.app.tournify.services;

import com.app.tournify.database.entities.*;
import com.app.tournify.database.repositories.LeagueStageRepository;
import com.app.tournify.dtos.NewEditLeagueStageDto;
import com.app.tournify.enums.LegType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LeagueStageService {

    private final LeagueStageRepository leagueStageRepository;
    private final TeamService teamService;
    private final MatchService matchService;
    private final RoundService roundService;

    public LeagueStageService(
            LeagueStageRepository leagueStageRepository,
            TeamService teamService,
            MatchService matchService,
            RoundService roundService
    ) {
        this.leagueStageRepository = leagueStageRepository;
        this.teamService = teamService;
        this.matchService = matchService;
        this.roundService = roundService;
    }

    @Transactional
    public List<LeagueStage> findAllLeagueStages() {
        return leagueStageRepository.findAll();
    }

    @Transactional
    public LeagueStage findLeagueStageById(Long id) {
        return leagueStageRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public LeagueStage saveLeagueStage(NewEditLeagueStageDto newEditLeagueStageDto) {
        LeagueStage leagueStageEntity = new LeagueStage(
                newEditLeagueStageDto.getName(),
                newEditLeagueStageDto.getType(),
                newEditLeagueStageDto.getLegType(),
                newEditLeagueStageDto.getWinPoints(),
                newEditLeagueStageDto.getDrawPoints(),
                newEditLeagueStageDto.getLossPoints());

        /* Populando times da liga */
        leagueStageEntity.setTable(teamService.findAllTeamsByIds(newEditLeagueStageDto.getTeamIds())
                .stream().map(TeamTable::new).toList());

        var rounds = buildRounds(leagueStageEntity.getTable(), leagueStageEntity.getLegType())
                .stream().map(round -> {
                    round.getMatches().forEach(matchService::save);

                    return roundService.save(round);
                }).toList();

        leagueStageEntity.setRounds(rounds);

        return leagueStageRepository.save(leagueStageEntity);
    }

    private List<Round> buildRounds(List<TeamTable> teams, LegType legType) {
        // Obtém o número de equipes
        int numberOfTeams = teams.size();
        int numberOfTeamsModule = numberOfTeams % 2 == 0 ? (numberOfTeams - 1) : numberOfTeams;
        // Lista para armazenar as rodadas de partidas
        var rounds = new ArrayList<Round>();

        // Contadores de jogos consecutivos em casa e fora para cada equipe
        int[] homeStreak = new int[numberOfTeams];
        int[] awayStreak = new int[numberOfTeams];

        // Gerar rodadas de ida
        for (int roundIndex = 0; roundIndex < numberOfTeamsModule; roundIndex++) {
            // Lista para armazenar as partidas da rodada atual
            List<Match> matches = new ArrayList<>();
            for (int i = 0; i < numberOfTeams / 2; i++) {
                // Calcula os índices das equipes que jogarão em casa e fora
                int homeIndex = (roundIndex + i) % numberOfTeamsModule;
                int awayIndex = (numberOfTeams - 1 - i + roundIndex) % numberOfTeamsModule;

                // Se o numero de times for par
                if (numberOfTeams % 2 == 0) {
                    // Se for a primeira iteração, ajusta o índice da equipe que jogará fora
                    if (i == 0) {
                        awayIndex = numberOfTeams - 1;
                    }
                } else { // Se o numero de times for impar
                    // Se for a mesma equipe, pula esta iteração
                    if (homeIndex == awayIndex) continue;
                }

                // Verifica e ajusta, se necessário, para evitar mais de 2 jogos consecutivos em casa ou fora
                if (homeStreak[homeIndex] == 2) {
                    // Troca de casa e fora se a equipe da casa já jogou 2 vezes seguidas em casa
                    int temp = homeIndex;
                    homeIndex = awayIndex;
                    awayIndex = temp;
                } else if (awayStreak[awayIndex] == 2) {
                    // Troca de casa e fora se a equipe de fora já jogou 2 vezes seguidas fora
                    int temp = homeIndex;
                    homeIndex = awayIndex;
                    awayIndex = temp;
                }

                // Adiciona a partida à lista de partidas da rodada
                matches.add(new Match(teams.get(homeIndex).getTeam(), teams.get(awayIndex).getTeam()));

                // Atualiza contadores de jogos consecutivos
                homeStreak[homeIndex]++;
                awayStreak[awayIndex]++;
                // Reseta o contador se a equipe jogar fora
                homeStreak[awayIndex] = 0;
                // Reseta o contador se a equipe jogar em casa
                awayStreak[homeIndex] = 0;
            }
            // Adiciona a rodada à lista de rodadas
            rounds.add(new Round(matches));
        }

        // Gerar rodadas de volta invertendo os times da ida
        if (legType.equals(LegType.TWO_LEGS)) {
            // Lista para armazenar as rodadas de volta
            List<Round> returnRounds = new ArrayList<>();
            for (Round round : rounds) {
                // Lista para armazenar as partidas da rodada de volta
                List<Match> matches = new ArrayList<>();
                for (Match match : round.getMatches()) {
                    // Inverte os times da partida
                    matches.add(new Match(match.getAway(), match.getHome()));
                }
                // Adiciona a rodada de volta à lista de rodadas de volta
                returnRounds.add(new Round(matches));
            }
            // Adiciona as rodadas de volta à lista de rodadas
            rounds.addAll(returnRounds);
        }

        // Retorna a lista de rodadas geradas
        return rounds;
    }
}
