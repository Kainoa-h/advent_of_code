## 6025 is too low
fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")

        dial = Dial(50)
        for line in lines:
            dial = turn_dial(dial, line)

        print(dial.seen_zero_count)


@fieldwise_init
struct Dial(Movable):
    var position: Int
    var seen_zero_count: Int

    fn __init__(out self, position: Int):
        self.position = position
        self.seen_zero_count = 0


fn turn_dial(read dial: Dial, read instruct: StringSlice) raises -> Dial:
    var steps = atol(instruct[1:])
    if instruct[0] == "L":
        steps *= -1
    var delta = dial.position + steps
    var new_position = delta % 100

    #     - If position == 0: crossings = |steps| // 100
    # - If position > 0 and |steps| >= position: crossings = (|steps| - position) // 100 + 1
    # - If position > 0 and |steps| < position: crossings = 0
    var met_zero_count: Int
    if steps < 0:
        var abs_steps = abs(steps)
        if dial.position == 0:
            met_zero_count = abs_steps // 100
        elif abs_steps >= dial.position:
            met_zero_count = (abs_steps - dial.position) // 100 + 1
        else:
            met_zero_count = 0
    else:
        met_zero_count = delta // 100

    # print(
    #     "steps: ",
    #     steps,
    #     ", position: ",
    #     new_position,
    #     ", delta: ",
    #     delta,
    #     ", rotations: ",
    #     met_zero_count,
    # )
    return Dial(new_position, met_zero_count + dial.seen_zero_count)
