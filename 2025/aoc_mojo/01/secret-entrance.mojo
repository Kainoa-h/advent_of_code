fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")

        safe = SafeDial(50)
        for line in lines:
            safe = turnDial(safe, line)

        print(safe.seenZeroCount)


@fieldwise_init
struct SafeDial(Movable):
    var indicator: Int
    var seenZeroCount: Int

    fn __init__(out self, indicator: Int):
        self.indicator = indicator
        self.seenZeroCount = 0


fn turnDial(read safe: SafeDial, read instruct: StringSlice) raises -> SafeDial:
    var turns = atol(instruct[1:])
    if instruct[0] == "L":
        turns *= -1
    var newIndicator = (safe.indicator + turns) % 100
    var newSeenZeroCount = (
        safe.seenZeroCount + 1 if newIndicator == 0 else safe.seenZeroCount
    )
    return SafeDial(newIndicator, newSeenZeroCount)
