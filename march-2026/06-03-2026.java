// 1784. Check if Binary String Has at Most One Segment of Ones

class Solution {
  public boolean checkOnesSegment(String s) {
    return !s.contains("01");
  }
}

