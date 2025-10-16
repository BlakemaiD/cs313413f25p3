package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * a visitor for drawing a shape to an android canvas
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // fixed
        this.paint = paint;   // fixed
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        // save current state, translate, visit child, and restore
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.restore();
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        // temporarily switch to fill style
        Style oldStyle = paint.getStyle();
        paint.setStyle(Style.FILL);
        f.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onOutline(final Outline o) {
        // temporarily switch to stroke style
        Style oldStyle = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int oldColor = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(oldColor);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final int n = s.getPoints().size();
        if (n < 2) return null;

        Path path = new Path();
        Point first = s.getPoints().get(0);
        path.moveTo(first.getX(), first.getY());
        for (int i = 1; i < n; i++) {
            Point p = s.getPoints().get(i);
            path.lineTo(p.getX(), p.getY());
        }
        path.close();
        canvas.drawPath(path, paint);
        return null;
    }
}
