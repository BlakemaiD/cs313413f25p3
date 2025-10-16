package edu.luc.etl.cs313.android.shapes.model;

import java.util.List;

/**
 * a special case of a group consisting only of points a closed polygon is a
 * shape defined by a list of points; the last point is connected to the first
 * one to close the polygon
 */
public final class Polygon extends Group {

    public Polygon(final Point... points) {
        super(points);
    }

    @SuppressWarnings("unchecked")
    public List<? extends Point> getPoints() {
        return (List<? extends Point>) getShapes();
    }

    @Override
    public <Result> Result accept(final Visitor<Result> v) {
        return v.onPolygon(this);
    }
}
