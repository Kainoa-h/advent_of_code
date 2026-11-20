package aoc2023;

import java.util.*;

class Day07 {

  public record GameSet(String handScore, String hand, int bid) {
  };

  static final char FIVE_OF_A_KIND = '6';
  static final char FOUR_OF_A_KIND = '5';
  static final char FULL_HOUSE = '4';
  static final char THREE_OF_A_KIND = '3';
  static final char TWO_PAIR = '2';
  static final char PAIR = '1';
  static final char HIGH_CARD = '0';

  static String getHandScore(String hand) {
    final int[] cards = new int[13];
    int twos = 0, three = 0, four = 0, five = 0;
    for (int i = 0; i < hand.length(); i++) {
      var c = hand.charAt(i);
      int idx = -1;
      if (Character.isDigit(c)) {
        idx = c - '0' - 1;
      } else if (c == 'A') {
        idx = 0;
      } else if (c == 'T') {
        idx = 9;
      } else if (c == 'J') {
        idx = 10;
      } else if (c == 'Q') {
        idx = 11;
      } else if (c == 'K') {
        idx = 12;
      }
      cards[idx]++;
      switch (cards[idx]) {
        case 5 -> {four--; five++;}
        case 4 -> {three--; four++;}
        case 3 -> {twos--; three++;}
        case 2 -> twos++;
      }
    }

    StringBuilder score = new StringBuilder();
    if (five > 0) {
      score.append(FIVE_OF_A_KIND);
    } else if (four > 0) {
      score.append(FOUR_OF_A_KIND);
    } else if (three > 0 && twos > 0) {
      score.append(FULL_HOUSE);
    } else if (three > 0) {
      score.append(THREE_OF_A_KIND);
    } else if (twos == 2) {
      score.append(TWO_PAIR);
    } else if (twos == 1) {
      score.append(PAIR);
    } else {
      score.append(HIGH_CARD);
    }

    for (int i = 0; i < hand.length(); i++) {
      var c = hand.charAt(i);
      if (Character.isDigit(c)) {
        score.append((char) (c - '0' - 2 + 'A'));
      } else if (c == 'T') {
        score.append('I');
      } else if (c == 'J') {
        score.append('J');
      } else if (c == 'Q') {
        score.append('K');
      } else if (c == 'K') {
        score.append('L');
      } else if (c == 'A') {
        score.append('M');
      }
    }

    return score.toString();
  }

  static long part1(String input) {
    var lines = input.split("\n");
    List<GameSet> gameSets = new ArrayList<>();
    for (var line : lines) {
      var hb = line.split(" ");
      var hand = hb[0];
      var bid = Integer.parseInt(hb[1]);
      gameSets.add(new GameSet(getHandScore(hand), hand, bid));
    }
    gameSets.sort(
        Comparator.comparing(GameSet::handScore)
            .thenComparing(GameSet::hand));
    var total = 0;
    for (int i = 0; i < gameSets.size(); i++) {
      total += (i + 1) * gameSets.get(i).bid;
    }
    return total;
  }
}
