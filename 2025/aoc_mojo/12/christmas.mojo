fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var count = 0
        for line in lines:
          if line[2] != "x":
            continue
          var segments = line.split(":")
          var grid = segments[0]
          var fill = segments[1].strip().split(" ")
          var wh = grid.split("x")
          var area = Int(wh[0])*Int(wh[1])
          var needed = 0
          for f in fill:
            needed += Int(f)
          needed *= 7
          if needed < area:
            count += 1
        print(count)

