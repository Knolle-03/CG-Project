package wpcg.bezier.algorithms;

import com.jme3.math.Vector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class QuickHull {

    public static List<Vector2f> calcConvexHull(List<Vector2f> points) {

        List<Vector2f> convexHull = new LinkedList<>();
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
        convexHull.add(convexHull.indexOf(v2), farthest);

        List<Vector2f> abovePC = new ArrayList<>();
        List<Vector2f> aboveCQ = new ArrayList<>();
        // sort point in above and below the line described by leftMost and rightMost
        //subList.remove(farthest);


        for (int i = 0; i < subList.size(); i++) {
            Vector2f current = subList.get(i);
            if (isInside(v1, v2, farthest, current)) {
                subList.remove(current);
            }
        }



        aboveAndBelow(subList, v1, farthest, abovePC, null);
        aboveAndBelow(subList, farthest, v2,  aboveCQ, null);

        findHull(abovePC, v1, farthest, convexHull);
        findHull(aboveCQ, farthest, v2, convexHull);
    }

    private static float calcDistanceToLine(Vector2f v1, Vector2f v2, Vector2f point) {

        float l2 = v1.distanceSquared(v2);
        if (l2 == 0) return point.distance(v1);

        Vector2f v1_point = new Vector2f(point.subtract(v1));
        Vector2f v1_v2 = new Vector2f(v1.subtract(v1));
        float res = v1_point.dot(v1_v2) / l2;
        float t = Math.max(0, Math.min(1, res));

        Vector2f projection = (v2.subtract(v1)).mult(t).add(v1);

        return point.distance(projection);

//        float slope = l2.y - l1.y / l2.y - l1.x;
//        float yIntersect = l1.y - slope * l1.x;
//        return Math.abs(-slope * point.x + point.y - yIntersect) / (float) Math.sqrt((slope * slope) + 1);
    }

    private static void aboveAndBelow(List<Vector2f> points, Vector2f leftMost, Vector2f rightMost, List<Vector2f> above, List<Vector2f> below) {
        System.out.println("Points size: " + points.size());
        int added = 0;
        //Vector2f v1 = new Vector2f(rightMost.x - leftMost.x, rightMost.y - leftMost.y);
        for (Vector2f point : points) {
            //Vector2f v2 = new Vector2f(rightMost.x - point.x, rightMost.y - point.y);
            float d = (point.x - leftMost.x) * (rightMost.y - leftMost.y)
                    - (point.y - leftMost.y) * (rightMost.x - leftMost.x);
            if (d > 0) {
                above.add(point);
                added++;
            }
            else {
                if (below != null) below.add(point);
            }
        }
        System.out.println("added: " + added);
    }

    /* A utility function to calculate area of triangle
   formed by (x1, y1) (x2, y2) and (x3, y3) */
    static double area(Vector2f v1, Vector2f v2, Vector2f v3) {
        return Math.abs((v1.x*(v2.y-v3.y) + v2.x*(v3.y-v1.y)+
                v3.x*(v1.y-v2.y))/2.0);
    }

    static boolean isInside(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f point) {
        /* Calculate area of triangle ABC */
        double A = area (v1, v2, v3);

        /* Calculate area of triangle PBC */
        double A1 = area (point, v2, v3);

        /* Calculate area of triangle PAC */
        double A2 = area (v1, point, v3);

        /* Calculate area of triangle PAB */
        double A3 = area (v1, v2, point);

        /* Check if sum of A1, A2 and A3 is same as A */
        return (A == A1 + A2 + A3);
    }

}