package Applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Point;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

class Arrow extends Canvas {
	
	private static final long serialVersionUID = 1L;
	Color clr;
	int radius;
	Point point;
	double angle;
	Dimension dim;
	Graphics gImg = null;
	Image mImg;

	public Arrow(Color color, int radius, int x, int y) {
		super();
		angle = Math.PI;
		clr = color;
		point = new Point(x, y);
		this.radius = radius;
		dim = new Dimension(2*radius, 2*radius);
		setMaximumSize(dim);
		setBounds(0, 0, dim.width, dim.height);
	}

	public void paint(Graphics g) {
		if (mImg != null)
			g.drawImage(mImg, 0, 0, null);
	}

	void prepareDraw(Graphics g) {
		g.setColor(clr);
	}

	void rotateBy(double angle) {
		this.angle = (this.angle + angle) % (2 * (Math.PI));
	}

	public void update(Graphics g) {		
		mImg = createImage(dim.width, dim.height);
		gImg = mImg.getGraphics();
		
		prepareDraw(gImg);
		draw(gImg);
		paint(g);
	}

	void draw(Graphics g) {
		int radX = (int) (radius * Math.sin(angle));
		int radY = (int) (radius * Math.cos(angle));

		int x = radius - radX;
		int y = radius - radY;
		int d = radius / 5;
		double a = 0.5;
		g.drawLine(radius, radius, x, y);

		int X[] = { x, (int) (x + d * Math.sin(angle + a)), (int) (x + d * Math.sin(angle - a)) };
		int Y[] = { y, (int) (y + d * Math.cos(angle + a)), (int) (y + d * Math.cos(angle - a)) };
		g.fillPolygon(X, Y, 3);
		// g.drawLine( point.x+radius, point.y, point.x+radius-5, point.y );
		// g.drawLine( point.x+radius, point.y, point.x+radius+5, point.y );
	}

	public Dimension getMinimumSize() {
		return dim;
	}

	public Dimension getPreferredSize() {
		return dim;
	}
}


class AppletThread extends Thread {
	AppletGraph pa = null;

	public AppletThread(AppletGraph pa) {
		super();
		this.pa = pa;
	}

	public void run() {
		int r=pa.c.radius;
		while (true) {
			try {
				Thread.sleep(3);
				pa.c.rotateBy(0.01);
				pa.c.repaint();				
				pa.c.setLocation(pa.c.point.x-r,pa.c.point.y-r);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	

}

public class AppletGraph extends Applet {
	
	private static final long serialVersionUID = 1L;
	static final int CX = 500, CY = 500;
	Arrow c;

	public Color getHtmlColor(String strRGB, Color def) {
		
		if (strRGB != null && strRGB.charAt(0) == '#') {
			try {
				return new Color(Integer.parseInt(strRGB.substring(1), 16));
			} catch (NumberFormatException e) {
				return def;
			}
		}
		return def;
	}
	public int getIntValue(String strInt,int def) {
		try {
			return Integer.parseInt(strInt);
		}catch(NumberFormatException e) {
			return def;
		}	
	}
	
	public void init() {
        setSize( CX, CY );
        setLayout( null );
        Color col = getHtmlColor(getParameter( "AppBkColor" ), new Color( 0, 0, 0 ));
        setBackground( col );
        Color colx = getHtmlColor(getParameter( "ArrowColor" ), new Color( 0, 255, 0 ));
        Point point =new Point(getIntValue(getParameter( "X" ),200),getIntValue(getParameter( "Y" ),200));
        int radius = getIntValue(getParameter( "Radius" ),150);
        c = new Arrow( colx, radius,point.x,point.y );
      
        add(c);
    }
	
	  public void start() {
	    	startThread();
	    }

	    public void stop() {
	    	stopThread();
	    }

	    public void destroy() {
	    	stopThread();
	    }

	    private AppletThread t = null;
	    private void createThread() {
	        if ( t == null ) {
	            t = new AppletThread( this );
	        }
	    }

	    private void startThread() {
	    	createThread();
	        t.start();
	    }

	    private void stopThread() {
	        if ( t != null ) {
	            t.interrupt();
	            t = null;
	        }
	    }
}
