package TUIO;

/*
 TUIO Java GUI Demo
 Copyright (c) 2005-2014 Martin Kaltenbrunner <martin@tuio.org>
 
 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files
 (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify, merge,
 publish, distribute, sublicense, and/or sell copies of the Software,
 and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:
 
 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import TUIO.*;
import java.io.IOException;

public class TuioDemoObject extends TuioObject {

    private Shape square;
    private static boolean isSquatsFileOpen = false;
    private static boolean isPushupsFileOpen = false;

    public TuioDemoObject(TuioObject tobj) {
        super(tobj);
        int size = TuioDemoComponent.object_size;
        square = new Rectangle2D.Float(-size / 2, -size / 2, size, size);

        AffineTransform transform = new AffineTransform();
        transform.translate(xpos, ypos);
        transform.rotate(angle, xpos, ypos);
        square = transform.createTransformedShape(square);
    }

    public void paint(Graphics2D g, int width, int height) {
        float Xpos = xpos * width;
        float Ypos = ypos * height;
        float scale = height / (float) TuioDemoComponent.table_size;

        AffineTransform trans = new AffineTransform();
        trans.translate(-xpos, -ypos);
        trans.translate(Xpos, Ypos);
        trans.scale(scale, scale);
        Shape s = trans.createTransformedShape(square);

        if (symbol_id == 1 || symbol_id == 2) {
            handleCommand(symbol_id);
        }

        // Optional: Visual rendering logic
        g.setPaint(symbol_id == 1 ? Color.blue : Color.green);
        g.fill(s);
        g.setPaint(Color.white);
        g.drawString(symbol_id + "", Xpos - 10, Ypos);
    }

    private void handleCommand(int symbol_id) {
        try {
            String command = "";
            if (symbol_id == 1 && !isSquatsFileOpen) {
                // Path to the squats CSV file
                command = "cmd /c start C:\\Users\\nourr\\Downloads\\exercise_squats.csv";
                isSquatsFileOpen = true;
            } else if (symbol_id == 2 && !isPushupsFileOpen) {
                // Path to the pushups CSV file
                command = "cmd /c start C:\\Users\\nourr\\Downloads\\exercise_pushups.csv";
                isPushupsFileOpen = true;
            }

            if (!command.isEmpty()) {
                Runtime.getRuntime().exec(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(TuioObject tobj) {
        float dx = tobj.getX() - xpos;
        float dy = tobj.getY() - ypos;
        float da = tobj.getAngle() - angle;

        if ((dx != 0) || (dy != 0)) {
            AffineTransform trans = AffineTransform.getTranslateInstance(dx, dy);
            square = trans.createTransformedShape(square);
        }

        if (da != 0) {
            AffineTransform trans = AffineTransform.getRotateInstance(da, tobj.getX(), tobj.getY());
            square = trans.createTransformedShape(square);
        }

        super.update(tobj);
    }
    
    public static void closeFile(int symbol_id) {
        if (symbol_id == 1) {
            isSquatsFileOpen = false;
        } else if (symbol_id == 2) {
            isPushupsFileOpen = false;
        }
    }
}
