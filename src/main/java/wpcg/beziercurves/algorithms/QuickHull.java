package wpcg.beziercurves.algorithms;

import com.jme3.math.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to calculate convex hull of a given list of points
 * source : https://en.wikipedia.org/wiki/Quickhull
 * @author Lennart Draeger
 */

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

        // add them to hull
        convexHull.add(leftMost);
        convexHull.add(rightMost);

        List<Vector2f> above = new ArrayList<>();
        List<Vector2f> below = new ArrayList<>();

        // sort points in above and below the line described by leftMost and rightMost
        aboveAndBelow(points, leftMost, rightMost, above, below);

        findHull(above, leftMost, rightMost, convexHull);
        findHull(below, rightMost, leftMost, convexHull);

        return convexHull;
    }


    private static void findHull(List<Vector2f> subList, Vector2f v1, Vector2f v2, List<Vector2f> convexHull) {
        if (subList.isEmpty()) return;

        // find farthest point from the line
        Vector2f farthest = subList.get(0);
        double longestDistance = calcDistanceToLine(v1, v2, farthest);
        for (int i = 1; i < subList.size(); i++) {
            Vector2f current = subList.get(i);
            double currentDistance = calcDistanceToLine(v1, v2, current);
            if (currentDistance > longestDistance) {
                longestDistance = currentDistance;
                farthest = current;
            }
        }
        // add the point to the hull between v1 and v2
        convexHull.add(convexHull.indexOf(v2), farthest);

        List<Vector2f> abovePC = new ArrayList<>();
        List<Vector2f> aboveCQ = new ArrayList<>();

        // remove points within the triangle from sublist
        // since they can't be in the convex hull
        for (int i = 0; i < subList.size(); i++) {
            Vector2f current = subList.get(i);
            if (isInTriangle(v1, v2, farthest, current)) {
                subList.remove(current);
            }
        }

         // sort points in above and below the line described by v1 and farthest respectively farthest and v2
        aboveAndBelow(subList, v1, farthest, abovePC, null);
        aboveAndBelow(subList, farthest, v2,  aboveCQ, null);

        // recursive entry
        findHull(abovePC, v1, farthest, convexHull);
        findHull(aboveCQ, farthest, v2, convexHull);
    }

    // ++++++++++++++++++++++ helper methods ++++++++++++++++++++++

    private static double calcDistanceToLine(Vector2f v1, Vector2f v2, Vector2f point) {
        return (Math.abs((v2.x - v1.x) * (v1.y - point.y) - (v1.x - point.x) * (v2.y - v1.y)) /
                Math.sqrt((Math.pow((v2.x - v1.x), 2) + Math.pow( v2.y - v1.y , 2)))  );
    }

    private static void aboveAndBelow(List<Vector2f> points, Vector2f leftMost, Vector2f rightMost, List<Vector2f> above, List<Vector2f> below) {
        for (Vector2f point : points) {
            float d = (point.x - leftMost.x) * (rightMost.y - leftMost.y)
                    - (point.y - leftMost.y) * (rightMost.x - leftMost.x);
            if (d > 0) above.add(point);
            else if (below != null) below.add(point);
        }
    }

    private static double calcTriangleArea(Vector2f v1, Vector2f v2, Vector2f v3) {
        return Math.abs((v1.x*(v2.y-v3.y) + v2.x*(v3.y-v1.y)+
                v3.x*(v1.y-v2.y))/2.0);
    }

    // check if a given point is within a triangle formed by v1, v2 and v3
    // for this to be true the sum of the triangles formed by the point and any two other point of the triangle
    // must be the size of the main triangle
    private static boolean isInTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f point) {
        double A = calcTriangleArea(v1, v2, v3);
        double A1 = calcTriangleArea(point, v2, v3);
        double A2 = calcTriangleArea(v1, point, v3);
        double A3 = calcTriangleArea(v1, v2, point);
        return (A == A1 + A2 + A3);
    }

}