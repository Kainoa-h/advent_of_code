from collections import Set


struct CircularQueue[T: Copyable & Movable & ImplicitlyCopyable]:
    var _data: List[Self.T]
    var _capacity: Int
    var _head: Int
    var _tail: Int
    var _count: Int

    fn __init__(out self, capacity: Int) raises:
        self._capacity = capacity
        self._head = 0
        self._tail = 0
        self._count = 0
        self._data = List[Self.T](capacity=capacity)

    fn enqueue(mut self, item: Self.T) raises:
        if self._count == self._capacity:
            raise Error("Queue is full")

        self._data[self._tail] = item
        self._tail = (self._tail + 1) % self._capacity
        self._count += 1

    fn dequeue(mut self) raises -> Self.T:
        if self._count == 0:
            raise Error("Queue is empty")

        var item = self._data[self._head]
        self._head = (self._head + 1) % self._capacity
        self._count -= 1
        return item

    fn is_empty(self) -> Bool:
        return self._count == 0

    fn is_full(self) -> Bool:
        return self._count == self._capacity

    fn size(self) -> Int:
        return self._count


@fieldwise_init
struct SearchState(Copyable, ImplicitlyCopyable, Movable):
    var lights: UInt16
    var press_index: UInt16
    var depth: UInt16


struct Puzzle(Movable, Stringable):
    var length: UInt16
    var solved_state: UInt16
    var buttons: List[UInt16]

    fn __init__(out self, str: StringSlice) raises:
        var segments = str.split(" ")
        var lights_str = segments[0][1:-1]
        self.length = len(lights_str)

        var light_bin: UInt16 = 0
        for i in range(len(lights_str)):
            if lights_str[i] == "#":
                light_bin = (light_bin << 1) | 1
            else:
                light_bin = light_bin << 1
        self.solved_state = light_bin

        self.buttons = List[UInt16]()
        for i in range(1, len(segments) - 1):
            var btn_effects = segments[i].strip("()").split(",")
            var btn_bin: UInt16 = 0
            for c in btn_effects:
                var shift_left: UInt16 = self.length - atol(c) - 1
                var mask: UInt16 = 1 << shift_left
                btn_bin |= mask
            self.buttons.append(btn_bin)

    fn __str__(self) -> String:
        var btns = ",".join([bin(x) for x in self.buttons])
        return (
            "Puzzle: "
            + bin(self.solved_state)
            + "\n"
            + "Buttons: "
            + btns
            + "\n"
        )


fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var btn_presses = 0
        for line in lines:
            var puzzle = Puzzle(line)
            btn_presses += Int(solve_puzzle(puzzle).value())
        print(btn_presses)



fn solve_puzzle(read puz: Puzzle) raises -> Optional[UInt16]:
    var queue = CircularQueue[SearchState](pow(2, Int(puz.length)))
    var visited = Set[UInt16]()

    queue.enqueue(SearchState(0, 0, 0))
    visited.add(0)

    while not queue.is_empty():
        var state = queue.dequeue()

        if state.lights == puz.solved_state:
            return state.depth

        for i in range(state.press_index, len(puz.buttons)):
            var mask = puz.buttons[i]
            var new_lights = state.lights ^ mask

            if new_lights not in visited:
                visited.add(new_lights)
                queue.enqueue(SearchState(new_lights, i + 1, state.depth + 1))

    return None
