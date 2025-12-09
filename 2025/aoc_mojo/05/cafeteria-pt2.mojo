@fieldwise_init
struct Range(Copyable, Movable, Comparable, Stringable, ImplicitlyCopyable):
  var start: Int64
  var end: Int64

  fn __str__(self) -> String:
    return "(" + String(self.start) + "-" + String(self.end) + ")"

  fn __lt__(self, other: Range) -> Bool:
    if self.start == other.start:
      return self.end < other.end
    return self.start < other.start

  fn __eq__(self, other: Range) -> Bool:
    return self.start == other.start and self.end == other.end

fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var segments = content.strip().split("\n\n")
        var fresh_ranges = segments[0].split("\n")
        var parsed_fresh_ranges = List[Range]()
        for fr in fresh_ranges:
          var seg = fr.split("-")
          parsed_fresh_ranges.append(Range(Int64(atol(seg[0])), (Int64(atol(seg[1])))))
        sort(parsed_fresh_ranges)
        # for x in parsed_fresh_ranges:
        #   print(String(x))
        # print("after comp:")
        # for x in collapse(parsed_fresh_ranges):
        #   print(String(x))
        var count:Int64 = 0
        for x in collapse(parsed_fresh_ranges):
          count += x.end - x.start + 1
        print(count)

fn collapse(read ranges: List[Range])-> List[Range]:
  var comps = List[Range]()
  var comp = ranges[0]
  for i in range(1, len(ranges)):
    var x = ranges[i]
    if (comp.end >= x.start):
      comp = Range(comp.start, max(comp.end, x.end))
    else:
      comps.append(comp)
      comp = x
  comps.append(comp)
  return comps^
