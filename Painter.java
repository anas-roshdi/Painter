import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.geom.Line2D;


public class FinalP extends JFrame {

    private Color currentColor = Color.BLACK;
    private boolean eraserMode = false;
    private int eraserSize = 10;
    private int penSize = 5;
     private final List<String> fontList = Arrays.asList("Arial", "Times New Roman", "Courier New", "Verdana", "Impact");

    public FinalP() {
       
        setTitle("Drawing App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom panel for drawing
        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.WHITE);
           drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               
                if (drawingPanel.isTextMode()) {
                    String text = JOptionPane.showInputDialog(this, "Enter text:");
                    drawingPanel.addText(e.getX(), e.getY(),text);
                }
            }
        });
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (eraserMode) {
                    drawingPanel.erasePoint(e.getX(), e.getY());
                } else if (drawingPanel.isPenMode()) {
                    drawingPanel.drawPoint(e.getX(), e.getY());
                }else if((!drawingPanel.circleMode)&&(!drawingPanel.lineMode)&&(!drawingPanel.rectangleMode)&&(!drawingPanel.polygonMode)){
                  if ( drawingPanel.selectShape instanceof Rectangle) {
                      Rectangle rect= (Rectangle) drawingPanel.selectShape;
                   rect.setLocation(e.getX() - drawingPanel.offsetX, e.getY() - drawingPanel.offsetY);
                   if(drawingPanel.fill){
                   drawingPanel.fillshapes.set(drawingPanel.indexOfselectShape, rect);}
                   else{drawingPanel.shapes.set(drawingPanel.indexOfselectShape, rect);}
                     repaint();
                 }
                  else if (drawingPanel.selectShape instanceof Line2D) {
    Line2D line = (Line2D) drawingPanel.selectShape;
    double newX1 = line.getX1() + (e.getX() - drawingPanel.offsetX);
    double newY1 = line.getY1() + (e.getY() - drawingPanel.offsetY);
    double newX2 = line.getX2() + (e.getX() - drawingPanel.offsetX);
    double newY2 = line.getY2() + (e.getY() - drawingPanel.offsetY);
    Line2D updatedLine = new Line2D.Double(newX1, newY1, newX2, newY2);
    
        drawingPanel.shapes.set(drawingPanel.indexOfselectShape, updatedLine);
    
}
                  else if ( drawingPanel.selectShape instanceof Ellipse2D) {
                     Ellipse2D circle= (Ellipse2D) drawingPanel.selectShape;
                   int newX = e.getX() - drawingPanel.offsetX;
                   int newY = e.getY() - drawingPanel.offsetY;
                   int width = (int)circle.getWidth();
                   int height =(int) circle.getHeight();
                   Ellipse2D updatedCircle = new Ellipse2D.Double(newX, newY, width, height);
                   if(drawingPanel.fill){
    drawingPanel.fillshapes.set(drawingPanel.indexOfselectShape, updatedCircle);}
                   else{drawingPanel.shapes.set(drawingPanel.indexOfselectShape, updatedCircle);}
    repaint();
                 }
                  else if (drawingPanel.selectShape instanceof Polygon) {
    Polygon polygon = (Polygon) drawingPanel.selectShape;
    int numPoints = polygon.npoints;
    
    int[] updatedXPoints = new int[numPoints];
    int[] updatedYPoints = new int[numPoints];
    
    for (int i = 0; i < numPoints; i++) {
        updatedXPoints[i] = polygon.xpoints[i] + (e.getX() - drawingPanel.offsetX - polygon.getBounds().x);
        updatedYPoints[i] = polygon.ypoints[i] + (e.getY() - drawingPanel.offsetY - polygon.getBounds().y);
    }
    
    Polygon updatedPolygon = new Polygon(updatedXPoints, updatedYPoints, numPoints);
    if(drawingPanel.fill){
    drawingPanel.fillshapes.set(drawingPanel.indexOfselectShape, updatedPolygon);}
    else{drawingPanel.shapes.set(drawingPanel.indexOfselectShape, updatedPolygon);}
    repaint();
}
                }
            }
        });
        Integer[] fontSizeList = {12, 16, 20, 24, 28};
JComboBox<Integer> fontSizeComboBox = new JComboBox<>(fontSizeList);
fontSizeComboBox.addActionListener(e -> {
    JComboBox<Integer> comboBox = (JComboBox<Integer>) e.getSource();
    Integer selectedFontSize = (Integer) comboBox.getSelectedItem();
    drawingPanel.setFontSize(selectedFontSize);
});
        // Create a color chooser
        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(this, "Choose Color", currentColor);
            if (selectedColor != null) {
                setCurrentColor(selectedColor);
            }
        });

        // Create a slider for eraser size
        JSlider eraserSizeSlider = new JSlider(1, 50, eraserSize);
        eraserSizeSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                setEraserSize(source.getValue());
            }
        });

        // Create an eraser toggle button
        JToggleButton eraserButton = new JToggleButton("Eraser");
        eraserButton.addActionListener(e -> {
            eraserMode = eraserButton.isSelected();
        });
        
        // Create a slider for pen size
        JSlider penSizeSlider = new JSlider(1, 20, penSize);
        penSizeSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                setPenSize(source.getValue());
            }
        });

        // Create the pen button
        JToggleButton penButton = new JToggleButton("Pen");
        penButton.addActionListener(e -> {
            boolean penMode = penButton.isSelected();
            drawingPanel.setPenMode(penMode);
        });
         // Create a button to add text
        JToggleButton addTextButton = new JToggleButton("Add Text");
        addTextButton.addActionListener(e -> {
            boolean textMode = addTextButton.isSelected();
            
            
                drawingPanel.setTextMode(textMode);
                
            
        });
        // Create a button to add lines
        JToggleButton lineButton = new JToggleButton("Line");
        lineButton.addActionListener(e -> {
            boolean lineMode = lineButton.isSelected();
            drawingPanel.setLineMode(lineMode);
        });
        // Create a button to add rectangle
        JToggleButton rectangleButton = new JToggleButton("Rectangle");
        rectangleButton.addActionListener(e -> {
            boolean rectangleMode = rectangleButton.isSelected();
            drawingPanel.setRectangleMode(rectangleMode);
        });
        // Create a button to add circle
        JToggleButton circleButton = new JToggleButton("Circle");
        circleButton.addActionListener(e -> {
            boolean circleMode = circleButton.isSelected();
            drawingPanel.setCircleMode(circleMode);
        });
        // Create a button to add polygon
        JToggleButton polygonButton = new JToggleButton("Polygon");
        polygonButton.addActionListener(e -> {
            boolean polygonMode = polygonButton.isSelected();
            drawingPanel.setPolygonMode(polygonMode);
        });
        // Create fill button
        JToggleButton fillButton = new JToggleButton("Fill");
        fillButton.addActionListener(e -> {
            boolean Fillselect = fillButton.isSelected();
            drawingPanel.setFillselect(Fillselect);
        });

        // Create a list of fonts
        JComboBox<String> fontComboBox = new JComboBox<>(fontList.toArray(new String[0]));
        fontComboBox.addActionListener(e -> {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedFont = (String) comboBox.getSelectedItem();
            drawingPanel.setFont(selectedFont);
        });
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(colorButton);
        controlPanel.add(new JLabel("Eraser Size:"));
        controlPanel.add(eraserSizeSlider);
        controlPanel.add(eraserButton);
        controlPanel.add(new JLabel("Pen Size:"));
        controlPanel.add(penSizeSlider);
        controlPanel.add(penButton);
        controlPanel.add(addTextButton);
        controlPanel.add(new JLabel("Font:"));
        controlPanel.add(fontComboBox);
        controlPanel.add(new JLabel("Font Size:"));
        controlPanel.add(fontSizeComboBox);
        JPanel controlPanel2 = new JPanel();
        controlPanel2.add(lineButton);
        controlPanel2.add(rectangleButton);
        controlPanel2.add(circleButton);
        controlPanel2.add(polygonButton);
        controlPanel2.add(fillButton);
        setLayout(new BorderLayout());
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(controlPanel2, BorderLayout.WEST);
    }

    private void setCurrentColor(Color color) {
        currentColor = color;
    }

    private void setEraserSize(int size) {
        eraserSize = size;
    }

    private void setPenSize(int size) {
        penSize = size;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FinalP frame = new FinalP();
            frame.setVisible(true);
        });
    }

    private class DrawingPanel extends JPanel {
        private boolean fill;
         private int offsetX;
         private int offsetY;
         private Shape selectShape;
         private int indexOfselectShape;
        private List<Shape> shapes = new ArrayList<>();
        private List<Shape> fillshapes = new ArrayList<>();
        private List<Color> colors1 = new ArrayList<>();
        private List<Color> colors2 = new ArrayList<>();
        private boolean lineMode = false;
        private int fontSize = 12;
        private boolean penMode = false;
        private boolean textMode = false;
        private boolean rectangleMode=false;
        private boolean circleMode=false;
        private boolean polygonMode=false;
        private boolean Fillselect=false;
        private int startX, startY;
        private final List<Point> points = new ArrayList<>();
        private String font = "Arial";
         public DrawingPanel() {
             offsetX = 0;
             offsetY = 0;
            addMouseListener(new MouseAdapter() {
                

                @Override
                public void mousePressed(MouseEvent e) {
                    if (lineMode) {
                        startX = e.getX();
                        startY = e.getY();
                    }
                    else if(rectangleMode){
                        startX = e.getX();
                        startY = e.getY();
                    }
                    else if(circleMode){
                        startX = e.getX();
                        startY = e.getY();
                    }
                    else if(polygonMode){
                        startX = e.getX();
                        startY = e.getY();
                    }
                    else{
                        for(Shape fillshape:fillshapes){
                            if (fillshape.contains(e.getPoint())) {
                                  selectShape=fillshape;
                                  indexOfselectShape=fillshapes.indexOf(fillshape);
                                  if (fillshape instanceof Rectangle) {
                                       Rectangle rect = (Rectangle) fillshape;
                                       offsetX = e.getX() - ((int)rect.getX());
                                       offsetY = e.getY() - ((int)rect.getY());
                                       fill=true;
                                  }else if (fillshape instanceof Ellipse2D) {
                                      Ellipse2D circle = (Ellipse2D) fillshape;
                                      offsetX = e.getX() - (int) circle.getX();
                                      offsetY = e.getY() - (int) circle.getY();
                                      fill=true;
                                  } else if (fillshape instanceof Polygon) {
                                     Polygon polygon = (Polygon) fillshape;
                                     offsetX = e.getX() - polygon.getBounds().x;
                                     offsetY = e.getY() - polygon.getBounds().y;
                                     fill=true;
                                  }
                                  break;
                }
                        }for(Shape shape:shapes){
                            if (shape.contains(e.getPoint())) {
                                  selectShape=shape;
                                  indexOfselectShape=shapes.indexOf(shape);
                                  if (shape instanceof Line2D) {
                                      Line2D line = (Line2D) shape;
                                      offsetX = e.getX() - (int) line.getX1();
                                      offsetY = e.getY() - (int) line.getY1();
}
                                  else if (shape instanceof Rectangle) {
                                       Rectangle rect = (Rectangle) shape;
                                       offsetX = e.getX() - ((int)rect.getX());
                                       offsetY = e.getY() - ((int)rect.getY());
                                       fill=false;
                                       
                                  }else if (shape instanceof Ellipse2D) {
                                      Ellipse2D circle = (Ellipse2D) shape;
                                      offsetX = e.getX() - (int) circle.getX();
                                      offsetY = e.getY() - (int) circle.getY();
                                      fill=false;
                                  } else if (shape instanceof Polygon) {
                                     Polygon polygon = (Polygon) shape;
                                     offsetX = e.getX() - polygon.getBounds().x;
                                     offsetY = e.getY() - polygon.getBounds().y;
                                     fill=false;
                                  }
                                  break;
                }
                        }
                        
                    }
                }

                @Override
                @SuppressWarnings("empty-statement")
                public void mouseReleased(MouseEvent e) {
                    Color cColor = currentColor;
                    if (lineMode) {
                        int endX = e.getX();
                        int endY = e.getY();
                         
                        shapes.add(new Line2D.Double(startX, startY, endX, endY));
                        colors1.add(cColor);
                        repaint();
                    }
                    else if(rectangleMode){
                        int endX = e.getX();
                        int endY = e.getY();
                        int rectX = Math.min(startX, endX);
                        int rectY = Math.min(startY, endY);
                        int rectWidth = Math.abs(endX - startX);
                        int rectHeight = Math.abs(endY - startY);
                        if(!Fillselect){
                        shapes.add(new Rectangle(rectX, rectY, rectWidth, rectHeight));
                        colors1.add(cColor);
                        repaint();}
                        else{
                            fillshapes.add(new Rectangle(rectX, rectY, rectWidth, rectHeight));
                            colors2.add(cColor);
                        repaint();
                        }
                            
                    }
                    else if(circleMode){
                        int endX = e.getX();
                        int endY = e.getY();
                        int cirX = Math.min(startX, endX);
                        int cirY = Math.min(startY, endY);
                        int cirRad = Math.abs(endX - startX);
                        if(!Fillselect){
                        shapes.add(new Ellipse2D.Double(cirX, cirY, cirRad, cirRad));
                        colors1.add(cColor);
                        repaint();}
                        else{fillshapes.add(new Ellipse2D.Double(cirX, cirY, cirRad, cirRad));
                        colors2.add(cColor);
                        repaint();}}
                 
                     else if(polygonMode){
                    
                        int[] xPoin = {startX, startX+50, e.getX(),startX+50 };
                        int[] yPoin = {startY,startY+50, e.getY(), startY-50};
                        if(!Fillselect){
                         shapes.add(new Polygon(xPoin, yPoin, 4));
                         colors1.add(cColor);
                        repaint();}
                        else{fillshapes.add(new Polygon(xPoin, yPoin, 4));
                        colors2.add(cColor);
                        repaint();}
                 
                    }
                     else{
                         selectShape=null;
                     }
                    
                }
            });
        }
         public void setFillselect(boolean Fillselect) {
            this.Fillselect = Fillselect;
        }
         public void setPolygonMode(boolean polygonMode) {
            this.polygonMode = polygonMode;
        }
         public void setRectangleMode(boolean rectangleMode) {
            this.rectangleMode = rectangleMode;
        }
         public void setCircleMode(boolean circleMode) {
            this.circleMode = circleMode;
        }

        public void setLineMode(boolean lineMode) {
            this.lineMode = lineMode;
        }
        public void addText(int x,int y ,String text) {
    points.add(new Point(x,y, text, new Font(font, Font.PLAIN, fontSize), currentColor));
    repaint();
}

        public void setFontSize(int size) {
    this.fontSize = size;
    setFont(new Font(font, Font.PLAIN, size));
}

public void setFont(String font) {
    this.font = font;
    setFont(new Font(font, Font.PLAIN, fontSize));
}


        public boolean isPenMode() {
            return penMode;
        }

        public void setPenMode(boolean penMode) {
            this.penMode = penMode;
        }
        public boolean isTextMode() {
            return textMode;
        }

        public void setTextMode(boolean textMode) {
            this.textMode = textMode;
        }

        @Override
       protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    for (Point point : points) {
        if (point.text != null) {
            g2d.setColor(point.color);
            g2d.setFont(point.font);
            g2d.drawString(point.text, point.x, point.y);
        } else {
            g2d.setColor(point.color);
            int diameter = point.penSize;
            int radius = diameter / 2;
            g2d.fill(new Ellipse2D.Double(point.x - radius, point.y - radius, diameter, diameter));
        }
        
    }
    for (int i = 0; i < shapes.size(); i++) {
        Shape shape = shapes.get(i);
        Color Color1 = colors1.get(i);
                if (shape instanceof Line2D) {
                    Line2D line = (Line2D) shape;
                    g2d.setColor(Color1);
                    
                    g2d.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
                }
                else if(shape instanceof Rectangle){
                    Rectangle rectangle = (Rectangle) shape;
                    g2d.setColor(Color1);
                    g2d.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
                else if(shape instanceof Ellipse2D){
                    Ellipse2D circle = (Ellipse2D) shape;
                    g2d.setColor(Color1);
                    g2d.drawOval((int)circle.getX(), (int)circle.getY(), (int)circle.getWidth(),(int)circle.getHeight());
                }
                else if(shape instanceof Polygon){
                    Polygon polygon = (Polygon) shape;
                    g2d.setColor(Color1);
                    g2d.drawPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
                }
            }
    for (int i = 0; i < fillshapes.size(); i++) {
        Shape fillshape = fillshapes.get(i);
        Color Color2 = colors2.get(i);
                
                if(fillshape instanceof Rectangle){
                    Rectangle rectangle = (Rectangle) fillshape;
                    g2d.setColor(Color2);
                        g2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                   
                }  else if(fillshape instanceof Ellipse2D){
                    Ellipse2D circle = (Ellipse2D) fillshape;
                    g2d.setColor(Color2);
                   g2d.fillOval((int)circle.getX(), (int)circle.getY(), (int)circle.getWidth(),(int)circle.getHeight());
                }
                else if(fillshape instanceof Polygon){
                    Polygon polygon = (Polygon) fillshape;
                    g2d.setColor(Color2);
                    g2d.fillPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
                }
                
            }
}
        public void drawPoint(int x, int y) {
    points.add(new Point(x, y, new Color(currentColor.getRGB()), penSize));
    repaint();
}

        public void erasePoint(int x, int y) {
            java.util.Iterator<Point> iterator = points.iterator();
            while (iterator.hasNext()) {
                Point point = iterator.next();
                if (point.distance(x, y) <= eraserSize) {
                    iterator.remove();
                }
            }
            repaint();
        }
    }

    private class Point extends java.awt.Point {
        private final Color color;
        private  String text = null;
        private  Font font = null;
        private  int penSize = 0;

        public Point(int x, int y, Color color, int penSize) {
            super(x, y);
            this.color = color;
                    this.penSize = penSize;
        }
        public Point(int x, int y, String text, Font font, Color color) {
            super(x, y);
            this.text = text;
            this.font = font;
            this.color = color;
        }
    }
    
    
}
