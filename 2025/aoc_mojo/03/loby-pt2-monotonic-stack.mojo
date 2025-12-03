fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var sum = 0
        for line in lines:
            sum += process(line)
        print(sum)
        print(sum == 169347417057382)


fn process(read line: StringSlice) raises -> Int:
    var bank: List[Int] = [atol(line[i]) for i in range(len(line))]
    var num_to_cull = len(bank) - 12
    var monotonic_stack = List[Int]()
    for i in range(len(bank)):
        var current = bank[i]
        # Keep popping as long as stack has items, we have culls,
        # and the top of stack is smaller than current
        while (
            len(monotonic_stack) > 0
            and num_to_cull > 0
            and monotonic_stack[-1] < current
        ):
            _ = monotonic_stack.pop()
            num_to_cull -= 1

        monotonic_stack.append(current)

    # If we didn't use all culls (e.g. "9876"), remove from the end
    for _i in range(num_to_cull):
        _ = monotonic_stack.pop()
    return atol("".join(monotonic_stack))


fn print_list(list: List[Int]):
    print("[", end="")
    for i in range(len(list)):
        print(list[i], end="")
        if i < len(list) - 1:
            print(", ", end="")
    print("]")


fn print_span(list: Span[Int]):
    print("[", end="")
    for i in range(len(list)):
        print(list[i], end="")
        if i < len(list) - 1:
            print(", ", end="")
    print("]")
