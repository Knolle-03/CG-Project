package wpcg.bezier.algorithms;

import com.jme3.math.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class QuickHull {

    public static List<Vector2f> calcConvexHull(List<Vector2f> points) {

        List<Vector2f> convexHull = new ArrayList<>();
        // calc outer most points
        Vector2f leftMost = points.get(0);
        Vector2f rightMost = points.get(0);
        for (int i = 1; i < points.size() ; i++) {
            Vector2f currentPoint = points.get(i);
            if (currentPoint.x < leftMost.x) leftMost = currentPoint;
            if (currentPoint.x > rightMost.x) rightMost = currentPoint;
        }
        convexHull.add(leftMost);
        convexHull.add(rightMost);


        List<Vector2f> above = new ArrayList<>();
        List<Vector2f> below = new ArrayList<>();
        // sort point in above and below the line described by leftMost and rightMost

        aboveAndBelow(points, leftMost, rightMost, above, below);


        findHull(above, leftMost, rightMost, convexHull);
        findHull(below, rightMost, leftMost, convexHull);
        return convexHull;
    }

    private static void findHull(List<Vector2f> subList, Vector2f v1, Vector2f v2, List<Vector2f> convexHull) {
        if (subList.isEmpty()) return;

        Vector2f farthest = subList.get(0);
        float longestDistance = calcDistanceToLine(v1, v2, farthest);
        for (int i = 1; i < subList.size(); i++) {
            Vector2f current = subList.get(i);
            float currentDistance = calcDistanceToLine(v1, v2, current);
            if (currentDistance > longestDistance) {
                longestDistance = currentDistance;
                farthest = current;
            }
        }
        int insertIndex = shiftRightSideByOne(v2, convexHull);
        convexHull.set(insertIndex, farthest);

        List<Vector2f> abovePC = new ArrayList<>();
        List<Vector2f> aboveCQ = new ArrayList<>();
        // sort point in above and below the line described by leftMost and rightMost
        //subList.remove(farthest);
        aboveAndBelow(subList, v1, farthest, abovePC, null);
        aboveAndBelow(subList, farthest, v2,  aboveCQ, null);

        findHull(abovePC, v1, farthest, convexHull);
        findHull(aboveCQ, farthest, v2, convexHull);
    }

    private static float calcDistanceToLine(Vector2f l1, Vector2f l2, Vector2f point) {
        float slope = l2.y - l1.y / l2.y - l1.x;
        float yIntersect = l1.y - slope * l1.x;
        return Math.abs(-slope * point.x + point.y - yIntersect) / (float) Math.sqrt((slope * slope) + 1);
    }

    private static int shiftRightSideByOne(Vector2f start, List<Vector2f> convexHull) {
        int firstNonNullIndex = -1;
        for (int i = convexHull.size() - 1; i > 0; i--) {
            if (convexHull.get(i) != null) {
                firstNonNullIndex = i;
                break;
            }
        }
        int startIndex = convexHull.indexOf(start);
        convexHull.add(null);
        for (int i = firstNonNullIndex; i >= startIndex ; i--) {
            Vector2f hullPoint = convexHull.get(i);
            convexHull.set(i + 1, hullPoint);
        }
        return startIndex;
    }

    private static void aboveAndBelow(List<Vector2f> points, Vector2f leftMost, Vector2f rightMost, List<Vector2f> above, List<Vector2f> below) {
        System.out.println("Points size: " + points.size());
        int added = 0;
        Vector2f v1 = new Vector2f(rightMost.x - leftMost.x, rightMost.y - leftMost.y);
        for (Vector2f point : points) {
            Vector2f v2 = new Vector2f(rightMost.x - point.x, rightMost.y - point.y);
            float xp = v1.x * v2.y - v1.y * v2.x;
            if (xp > 0) {
                above.add(point);
                added++;
            }
            else {
                if (below != null) below.add(point);
            }
        }
        System.out.println("added: " + added);
    }

}


//    private List<Vector2f> calcConvexHull(List<Vector2f> points) {
//        System.out.println("start-----------------");
//        List<Vector2f> convexHull = new ArrayList<>();
//        // calc outer most points
//        Vector2f leftMost = points.get(0);
//        Vector2f rightMost = points.get(0);
//        for (int i = 1; i < points.size() ; i++) {
//            Vector2f currentPoint = points.get(i);
//            if (currentPoint.x < leftMost.x) leftMost = currentPoint;
//            if (currentPoint.x > rightMost.x) rightMost = currentPoint;
//        }
//        convexHull.add(leftMost);
//        convexHull.add(rightMost);
//
//
//        List<Vector2f> above = new ArrayList<>();
//        List<Vector2f> below = new ArrayList<>();
//        // sort point in above and below the line described by leftMost and rightMost
//
//        aboveAndBelow(points, leftMost, rightMost, above, below);
//
//
//        findHull(above, leftMost, rightMost, convexHull);
//        findHull(below, rightMost, leftMost, convexHull);
//        return convexHull;
//    }
//
//    private void findHull(List<Vector2f> subList, Vector2f v1, Vector2f v2, List<Vector2f> convexHull) {
//        if (subList.isEmpty()) return;
//
//        Vector2f farthest = subList.get(0);
//        float longestDistance = calcDistanceToLine(v1, v2, farthest);
//        for (int i = 1; i < subList.size(); i++) {
//            Vector2f current = subList.get(i);
//            float currentDistance = calcDistanceToLine(v1, v2, current);
//            if (currentDistance > longestDistance) {
//                longestDistance = currentDistance;
//                farthest = current;
//            }
//        }
//        int insertIndex = shiftRightSideByOne(v2, convexHull);
//        convexHull.set(insertIndex, farthest);
//
//        List<Vector2f> abovePC = new ArrayList<>();
//        List<Vector2f> aboveCQ = new ArrayList<>();
//        // sort point in above and below the line described by leftMost and rightMost
//        //subList.remove(farthest);
//        aboveAndBelow(subList, v1, farthest, abovePC, null);
//        aboveAndBelow(subList, farthest, v2,  aboveCQ, null);
//
//        findHull(abovePC, v1, farthest, convexHull);
//        findHull(aboveCQ, farthest, v2, convexHull);
//    }
//
//    public float calcDistanceToLine(Vector2f l1, Vector2f l2, Vector2f point) {
//        float slope = l2.y - l1.y / l2.y - l1.x;
//        float yIntersect = l1.y - slope * l1.x;
//        return Math.abs(-slope * point.x + point.y - yIntersect) / (float) Math.sqrt((slope * slope) + 1);
//    }
//
//    private int shiftRightSideByOne(Vector2f start, List<Vector2f> convexHull) {
//        int firstNonNullIndex = -1;
//        for (int i = convexHull.size() - 1; i > 0; i--) {
//            if (convexHull.get(i) != null) {
//                firstNonNullIndex = i;
//                break;
//            }
//        }
//        int startIndex = convexHull.indexOf(start);
//        convexHull.add(null);
//        for (int i = firstNonNullIndex; i >= startIndex ; i--) {
//            Vector2f hullPoint = convexHull.get(i);
//            convexHull.set(i + 1, hullPoint);
//        }
//        return startIndex;
//    }
//
//    private void aboveAndBelow(List<Vector2f> points, Vector2f leftMost, Vector2f rightMost, List<Vector2f> above, List<Vector2f> below) {
//        System.out.println("Points size: " + points.size());
//        int added = 0;
//        Vector2f v1 = new Vector2f(rightMost.x - leftMost.x, rightMost.y - leftMost.y);
//        for (Vector2f point : points) {
//            Vector2f v2 = new Vector2f(rightMost.x - point.x, rightMost.y - point.y);
//            float xp = v1.x * v2.y - v1.y * v2.x;
//            if (xp > 0) {
//                above.add(point);
//                added++;
//            }
//            else {
//                if (below != null) below.add(point);
//            }
//        }
//        System.out.println("added: " + added);
//    }