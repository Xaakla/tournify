package com.app.tournify.services;

import com.app.tournify.database.entities.*;
import com.app.tournify.database.repositories.LeagueStageRepository;
import com.app.tournify.dtos.NewEditLeagueStageDto;
import com.app.tournify.enums.LegType;
import jakarta.transaction.Transactional;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        int numberOfTeams = teams.size();
        var rounds = new ArrayList<Round>();

        var tempTeams = teams;
        // Loop through all rounds
        for (int roundIndex = 0; roundIndex < numberOfTeams - 1; roundIndex++) {
            var matches = new ArrayList<Match>();

            var result = orderHomeAndAwayTeams(roundIndex, rounds, tempTeams.get(0), tempTeams.get(1));

            matches.add(new Match(result.getFirst().getTeam(), result.getSecond().getTeam()));

            // Loop through all matches in a single round
            for (int j = 1; j < numberOfTeams / 2; j++) {
                var response = orderHomeAndAwayTeams(roundIndex, rounds, tempTeams.get(j + 1), tempTeams.get(tempTeams.size() - j));

                matches.add(new Match(response.getFirst().getTeam(), response.getSecond().getTeam()));
            }

            var cycle = new ArrayList<TeamTable>(teams);
            for (int i = 0; i < tempTeams.size(); i++) {
                if (i == 0) {
                    cycle.set(0, tempTeams.get(i));
                } else if (i >= tempTeams.size() - 1) {
                    cycle.set(1, tempTeams.get(i));
                } else {
                    cycle.set(i + 1, tempTeams.get(i));
                }
            }
            tempTeams = cycle;

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

        return rounds;
    }

    private Pair<TeamTable, TeamTable> orderHomeAndAwayTeams(int roundIndex, List<Round> rounds, TeamTable team1, TeamTable team2) {
        Match team1LastMatch = null, team2LastMatch = null,
                team1PenultimateMatch = null, team2PenultimateMatch = null,
                team1AntiPenultimateMatch = null, team2AntiPenultimateMatch = null;

        TeamTable nextHomeTeam = team1;
        TeamTable nextAwayTeam = team2;

        // Caso o round atual não for o primeiro, ou seja, cai nesse if apenas se o round atual for do segundo pra frente
        if (roundIndex > 0) {
            // Busque a ultima partida do time1
            team1LastMatch = findPreviousMatchByTeamId(rounds, roundIndex, team1.getTeam().getId(), 1);

            // Busque a ultima partida do time2
            team2LastMatch = findPreviousMatchByTeamId(rounds, roundIndex, team2.getTeam().getId(), 1);

            // Caso o round atual for do terceiro pra frente
            if (roundIndex > 1) {
                // Busque a penultima partida do time1
                team1PenultimateMatch = findPreviousMatchByTeamId(rounds, roundIndex, team1.getTeam().getId(), 2);

                // Busque a penultima partida do time2
                team2PenultimateMatch = findPreviousMatchByTeamId(rounds, roundIndex, team2.getTeam().getId(), 2);

                // Caso o round atual for do quarto pra frente, ou seja, caso tenha uma anti penultima partida para buscar
                if (roundIndex > 2) {
                    // Busque a anti penultima partida do time1
                    team1AntiPenultimateMatch = findPreviousMatchByTeamId(rounds, roundIndex, team1.getTeam().getId(), 3);

                    // Busque a anti penultima partida do time2
                    team2AntiPenultimateMatch = findPreviousMatchByTeamId(rounds, roundIndex, team2.getTeam().getId(), 3);
                }
            }

            if (team1LastMatch != null && team2LastMatch != null) {
                // Se na ultima partida do time1, ele jogou em casa
                if (Objects.equals(team1LastMatch.getHome().getId(), team1.getTeam().getId())) {
                    // Se na ultima partida do time2, ele jogou em casa
                    if (Objects.equals(team2LastMatch.getHome().getId(), team2.getTeam().getId())) { // Só cai nesse if se ambos os times jogaram dentro de casa na ultima partida
                        // Verificando se existe uma penultima rodada para olhar o historico de partidas
                        if (team1PenultimateMatch != null && team2PenultimateMatch != null) {
                            // Se no penultimo jogo do team1, ele jogou em casa
                            if (Objects.equals(team1PenultimateMatch.getHome().getId(), team1.getTeam().getId())) {
                                // Se no penultimo jogo do team2, ele tambem jogou em casa
                                if (Objects.equals(team2PenultimateMatch.getHome().getId(), team2.getTeam().getId())) { // Só cai nesse if se ambos os times jogaram dentro de casa na ultima partida
                                    // Verificando se existe uma anti penultima rodada para olhar o historico de partidas
                                    if (team1AntiPenultimateMatch != null && team2AntiPenultimateMatch != null) {
                                        // Se no anti penultimo jogo do team1, ele jogou em casa
                                        if (Objects.equals(team1AntiPenultimateMatch.getHome().getId(), team1.getTeam().getId())) {
                                            // Se no anti penultimo jogo do team2, ele tambem jogou em casa
                                            if (Objects.equals(team2AntiPenultimateMatch.getHome().getId(), team2.getTeam().getId())) { // Só cai nesse if se ambos os times jogaram dentro de casa na anti penultima partida
                                                // Sorteia quem joga em casa e quem joga fora
                                                // todo
                                            } else { // Só cai nesse else caso o time1 jogou em casa na anti penultima partida e o time2 jogou fora
                                                // Como o team1 jogou em casa na anti penultima e o team2 jogou fora...
                                                // Agora o team2 joga dentro de casa e o team1 joga fora
                                                nextHomeTeam = team2;
                                                nextAwayTeam = team1;
                                            }
                                        } else { // Só cai nesse if se o team1 jogou a anti penultima partida fora de casa
                                            // Verificando se na anti penultima partida o team2 tambem jogou fora de casa
                                            if (Objects.equals(team2AntiPenultimateMatch.getAway().getId(), team2.getTeam().getId())) {
                                                // Sorteia quem joga em casa e quem joga fora
                                                // todo
                                            } else {
                                                nextHomeTeam = team1; // talvez remova
                                                nextAwayTeam = team2; // talvez remova
                                            }
                                        }
                                    } else { // Se nao houver anti penultima rodada, sorteia quem joga em casa e quem joga fora
                                        // Sorteia quem joga em casa e quem joga fora
                                        // todo
                                    }
                                } else { // Só cai nesse else caso o time1 jogou em casa na penultima partida e o time2 jogou fora
                                    // Como o team1 jogou em casa na penultima e o team2 jogou fora...
                                    // Agora o team2 joga dentro de casa e o team1 joga fora
                                    nextHomeTeam = team2;
                                    nextAwayTeam = team1;
                                }
                            } else { // Só ai nesse if se o team1 jogou a penultima rodada fora de casa
                                // Verificando se na penultima partida o team2 tambem jogou fora de casa
                                if (Objects.equals(team2PenultimateMatch.getAway().getId(), team2.getTeam().getId())) { // Só cai nesse if se ambos os times jogaram fora de casa na penultima partida
                                    // Verificando se existe anti penultima partida para olhar o historico de partidas
                                    if (team1AntiPenultimateMatch != null && team2AntiPenultimateMatch != null) {
                                        if (Objects.equals(team1AntiPenultimateMatch.getHome().getId(), team1.getTeam().getId())) {
                                            if (Objects.equals(team2AntiPenultimateMatch.getHome().getId(), team2.getTeam().getId())) {
                                                // Sorteia quem joga em casa e quem joga fora
                                                // todo
                                            } else {
                                                nextHomeTeam = team2;
                                                nextAwayTeam = team1;
                                            }
                                        } else { // Só cai nesse else caso o team1 tenha jogado fora de casa a anti penultima partida
                                            // Verifica se o team2 tambem jogou a anti penultima partida fora de casa
                                            if (Objects.equals(team2AntiPenultimateMatch.getAway().getId(), team2.getTeam().getId())) {
                                                // Sorteia quem joga em casa e quem joga fora
                                                // todo
                                            } else {
                                                nextHomeTeam = team1; // talvez remova
                                                nextAwayTeam = team2; // talvez remova
                                            }
                                        }
                                    } else {
                                        // Sorteia quem joga em casa e quem joga fora
                                        // todo
                                    }
                                } else {
                                    nextHomeTeam = team1; // talvez remova
                                    nextAwayTeam = team2; // talvez remova
                                }
                            }
                        } else { // Se nao houver penultima rodada, sorteia quem joga em casa e quem joga fora
                            // todo
                        }
                    } else { // Só cai nesse else caso o time1 jogou em casa na ultima partida e o time2 jogou fora
                        // Como o team1 jogou em casa na ultima e o team2 jogou fora...
                        // Agora o team2 joga dentro de casa e o team1 joga fora
                        nextHomeTeam = team2;
                        nextAwayTeam = team1;
                    }
                } else { // Só cai nesse else caso o team 1 tenha jogado a ultima partida fora de casa
                    // Verifica se o team2 jogou fora a ultima partida tambem
                    if (Objects.equals(team2LastMatch.getAway().getId(), team2.getTeam().getId())) {
                        // Verifica se tem penultima partida
                        if (team1PenultimateMatch != null && team2PenultimateMatch != null) {
                            // Verifica se o team1 jogou em casa na penultima partida
                            if (Objects.equals(team1PenultimateMatch.getHome().getId(), team1.getTeam().getId())) {
                                if (Objects.equals(team2PenultimateMatch.getHome().getId(), team2.getTeam().getId())) {
                                    if (team1AntiPenultimateMatch != null && team2AntiPenultimateMatch != null) {
                                        if (Objects.equals(team1AntiPenultimateMatch.getHome().getId(), team1.getTeam().getId())) {
                                            if (Objects.equals(team2AntiPenultimateMatch.getHome().getId(), team2.getTeam().getId())) {
                                                // Sorteia quem joga em casa e quem joga fora
                                                // todo
                                            } else {
                                                nextHomeTeam = team2;
                                                nextAwayTeam = team1;
                                            }
                                        } else {
                                            if (Objects.equals(team2AntiPenultimateMatch.getAway().getId(), team2.getTeam().getId())) {
                                                // Sorteia quem joga em casa e quem joga fora
                                                // todo
                                            } else {
                                                nextHomeTeam = team1; // talvez remova
                                                nextAwayTeam = team2; // talvez remova
                                            }
                                        }
                                    } else {
                                        // Sorteia quem joga em casa e quem joga fora
                                        // todo
                                    }
                                } else {
                                    nextHomeTeam = team2;
                                    nextAwayTeam = team1;
                                }
                            } else { // Só cai nesse else se o team1 jogou fora na penultima partida
                                // Verifica se o team2 jogou fora de casa na penultima partida
                                if (Objects.equals(team2PenultimateMatch.getAway().getId(), team2.getTeam().getId())) {
                                    // Sorteia quem joga em casa e quem joga fora
                                    // todo
                                } else {
                                    nextHomeTeam = team1; // talvez remova
                                    nextAwayTeam = team2; // talvez remova
                                }
                            }
                        } else {
                            // Sorteia quem joga em casa e quem joga fora
                            // todo
                        }
                    } else { // Só cai nesse else caso o team1 tenha jogado fora a ultima partida e o team2 tenha jogado em casa
                        nextHomeTeam = team1; // talvez remova
                        nextAwayTeam = team2; // talvez remova
                    }
                }
            }
        }

        return Pair.of(nextHomeTeam, nextAwayTeam);
    }

    private Match findPreviousMatchByTeamId(List<Round> rounds, int roundIndex, Long teamId, int previousRoundIndex) {
        return rounds.get(roundIndex - previousRoundIndex).getMatches()
                .stream().filter(match ->
                        Objects.equals(match.getHome().getId(), teamId) ||
                                Objects.equals(match.getAway().getId(), teamId)
                ).findFirst().orElse(null);
    }
}
