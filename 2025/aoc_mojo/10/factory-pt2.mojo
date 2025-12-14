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
struct BitSearchState[T: Copyable & ImplicitlyCopyable & Movable](
    Copyable, ImplicitlyCopyable, Movable
):
    var lights: Self.T
    var press_index: UInt16
    var depth: UInt16


@fieldwise_init
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

        var total = 0
        for line in lines:
            var segments = line.split(" ")
            var final_state_str = segments[-1][1:-1]
            var target = [atol(x) for x in final_state_str.split(",")]
            var length = len(target)

            # Parse button effects once
            var button_effects_list = List[List[Int]]()
            var button_masks = List[UInt16]()
            for i in range(1, len(segments) - 1):
                var btn_effects = [
                    atol(x) for x in segments[i].strip("()").split(",")
                ]
                var btn_bin: UInt16 = 0
                for c in btn_effects:
                    var shift_left: UInt16 = length - c - 1
                    var mask: UInt16 = 1 << shift_left
                    btn_bin |= mask
                button_masks.append(btn_bin)
                button_effects_list.append(btn_effects^)

            # Find minimum using recursive search over all parity solutions
            var result = solve_recursive(target, button_effects_list, button_masks, length)
            total += result
            
            if result < 0:
                print("Unsolvable!")
        print(total)


fn solve_recursive(
    target: List[Int],
    button_effects_list: List[List[Int]],
    button_masks: List[UInt16],
    length: Int,
) raises -> Int:
    """Recursively solve by trying all parity solutions and finding minimum total."""
    
    # Base case: check if all zeros
    var all_zero = True
    for x in target:
        if x != 0:
            all_zero = False
            break
    if all_zero:
        return 0
    
    # Check for negative values (invalid state)
    for x in target:
        if x < 0:
            return -1
    
    # Get current parity pattern
    var parity_bin: UInt16 = 0
    for x in target:
        parity_bin = (parity_bin << 1) | (x % 2)
    
    # Find ALL solutions that achieve this parity
    var all_solutions = find_all_parity_solutions(parity_bin, button_masks, length)
    
    if len(all_solutions) == 0:
        return -1
    
    var min_total = -1
    
    # Try each parity solution and recurse
    for solution in all_solutions:
        ref buttons_pressed = solution
        var num_presses = len(buttons_pressed)
        
        # Create new target after subtracting button effects
        var new_target = [x for x in target]
        for btn_idx in buttons_pressed:
            for affected_idx in button_effects_list[btn_idx]:
                new_target[affected_idx] -= 1
        
        # Halve all values
        for i in range(len(new_target)):
            new_target[i] //= 2
        
        # Recurse with multiplier effect (each press in recursion counts as 2)
        var sub_result = solve_recursive(
            new_target, button_effects_list, button_masks, length
        )
        
        if sub_result >= 0:
            var total = num_presses + 2 * sub_result
            if min_total < 0 or total < min_total:
                min_total = total
    
    return min_total


fn find_all_parity_solutions(
    target_parity: UInt16,
    button_masks: List[UInt16],
    length: Int,
) raises -> List[List[Int]]:
    """Find ALL button combinations that achieve the target parity pattern."""
    
    var solutions = List[List[Int]]()
    var num_buttons = len(button_masks)
    
    # Try all 2^num_buttons combinations
    var num_combinations = 1 << num_buttons
    
    for combo in range(num_combinations):
        var parity: UInt16 = 0
        var buttons_used = List[Int]()
        
        for btn in range(num_buttons):
            if (combo >> btn) & 1:
                parity ^= button_masks[btn]
                buttons_used.append(btn)
        
        if parity == target_parity:
            solutions.append(buttons_used^)
    
    return solutions^


fn solve_puzzle_mask(read puz: Puzzle) raises -> Optional[UInt16]:
    var queue = CircularQueue[BitSearchState[UInt16]](pow(2, Int(puz.length)))
    var visited = Set[UInt16]()

    queue.enqueue(BitSearchState[UInt16](0, 0, 0))
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
                queue.enqueue(
                    BitSearchState(new_lights, i + 1, state.depth + 1)
                )

    return None
