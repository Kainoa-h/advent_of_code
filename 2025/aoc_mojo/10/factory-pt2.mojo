from collections import Set, Dict


fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var total_score = 0

        for line_idx in range(len(lines)):
            var line = lines[line_idx]
            var segments = line.split(" ")
            var final_state_str = segments[-1][1:-1]
            var goal = [atol(x) for x in final_state_str.split(",")]
            var num_variables = len(goal)

            # Parse button coefficients as binary tuples (1 if affects index, 0 otherwise)
            var coeffs = List[List[Int]]()
            for i in range(1, len(segments) - 1):
                var btn_indices = [
                    atol(x) for x in segments[i].strip("()").split(",")
                ]
                var coeff = List[Int]()
                for j in range(num_variables):
                    var found = False
                    for idx in btn_indices:
                        if idx == j:
                            found = True
                            break
                    if found:
                        coeff.append(1)
                    else:
                        coeff.append(0)
                coeffs.append(coeff^)

            # Precompute all patterns
            var pattern_costs = compute_patterns(coeffs, num_variables)
            
            # Solve with memoization
            var memo = Dict[String, Int]()
            var subscore = solve_single_aux(goal, pattern_costs, num_variables, memo)
            
            total_score += subscore

        print(total_score)


fn list_to_string(lst: List[Int]) -> String:
    """Convert list to string for use as dict key."""
    var parts = List[String]()
    for x in lst:
        parts.append(String(x))
    return ",".join(parts)


fn compute_patterns(
    coeffs: List[List[Int]], 
    num_variables: Int
) raises -> Dict[String, Dict[String, Int]]:
    var num_buttons = len(coeffs)
    var out = Dict[String, Dict[String, Int]]()
    
    # Initialize all parity patterns with empty dicts
    # There are 2^num_variables parity patterns
    var num_parity_patterns = 1 << num_variables
    for p in range(num_parity_patterns):
        var parity_list = List[Int]()
        for i in range(num_variables):
            parity_list.append((p >> (num_variables - 1 - i)) & 1)
        var parity_key = list_to_string(parity_list)
        out[parity_key] = Dict[String, Int]()
    
    # Try all 2^num_buttons button combinations
    var num_combinations = 1 << num_buttons
    for combo in range(num_combinations):
        # Compute the effect pattern (sum of coefficients for pressed buttons)
        var pattern = List[Int]()
        for _i in range(num_variables):
            pattern.append(0)
        
        var num_pressed = 0
        for btn in range(num_buttons):
            if (combo >> btn) & 1:
                num_pressed += 1
                for i in range(num_variables):
                    pattern[i] += coeffs[btn][i]
        
        # Compute parity pattern
        var parity_list = List[Int]()
        for i in range(num_variables):
            parity_list.append(pattern[i] % 2)
        
        var parity_key = list_to_string(parity_list)
        var pattern_key = list_to_string(pattern)
        
        # Only store if this is a new pattern or has lower cost
        if pattern_key not in out[parity_key]:
            out[parity_key][pattern_key] = num_pressed
        elif num_pressed < out[parity_key][pattern_key]:
            out[parity_key][pattern_key] = num_pressed
    
    return out^


fn solve_single_aux(
    goal: List[Int],
    pattern_costs: Dict[String, Dict[String, Int]],
    num_variables: Int,
    mut memo: Dict[String, Int],
) raises -> Int:
    """Recursive solver with memoization."""
    
    # Check if all zeros (base case)
    var all_zero = True
    for x in goal:
        if x != 0:
            all_zero = False
            break
    if all_zero:
        return 0
    
    # Check memo
    var goal_key = list_to_string(goal)
    if goal_key in memo:
        return memo[goal_key]
    
    # Compute parity of goal
    var parity_list = List[Int]()
    for x in goal:
        parity_list.append(x % 2)
    var parity_key = list_to_string(parity_list)
    
    var answer = 1000000
    
    # Get all patterns for this parity
    ref patterns_for_parity = pattern_costs[parity_key]
    
    # Try each pattern
    for item in patterns_for_parity.items():
        var pattern_key = item.key
        var pattern_cost = item.value
        
        # Parse pattern from string
        var pattern_parts = pattern_key.split(",")
        var pattern = List[Int]()
        for p in pattern_parts:
            pattern.append(atol(p))
        
        # Check if pattern <= goal (element-wise)
        var valid = True
        for i in range(num_variables):
            if pattern[i] > goal[i]:
                valid = False
                break
        
        if valid:
            # Compute new goal: (goal - pattern) / 2
            var new_goal = List[Int]()
            for i in range(num_variables):
                new_goal.append((goal[i] - pattern[i]) // 2)
            
            var sub_result = solve_single_aux(new_goal, pattern_costs, num_variables, memo)
            var total = pattern_cost + 2 * sub_result
            if total < answer:
                answer = total
    
    memo[goal_key] = answer
    return answer
