
package PaqueteVideo2DistanciaYDireccion;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import org.jfree.data.xy.DefaultXYDataset;


public class Estadisticas {

	public static int PartidasTotales=10000;
	
	public static int numPartidas = 0;
	public static double[][] datos = new double[2][PartidasTotales];
	
	
	public static void meterDato(double timestep) {
		datos[0][numPartidas]=numPartidas+1;
		datos[1][numPartidas]=timestep;
	}
	

	public static void graficar()
	{
		DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Progreso", datos);

       /* XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.ORANGE);
        renderer.setSeriesStroke(0, new BasicStroke(1));
       // renderer.*/

        JFreeChart chart = ChartFactory.createXYLineChart("Evolucion del aprendizaje", "Partidas jugadas", "TimeSteps", dataset);
       // chart.getXYPlot().getRangeAxis().setRange(0, numPartidas + 50);
       // ((NumberAxis) chart.getXYPlot().getRangeAxis()).setNumberFormatOverride(new DecimalFormat("#'%'"));
        //chart.getXYPlot().setRenderer(renderer);

        BufferedImage image = chart.createBufferedImage(600, 400);
        try {
			ImageIO.write(image, "png", new File("evolución del aprendizaje.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("FALLO: La imagen no ha podido ser creada.");
		}
	}


	public static double[][] hacerMediaDatos(int paso){
		double[][] datosMedios = new double[2][PartidasTotales/paso];
		double suma = 0;
		
		int nuevoNumPartidas = 0;
		/*System.out.println("La longitud de datos es: "+datos.length);
		System.out.println("-----------------------------------------");*/
		for (int i = 0; i < datos[0].length; i++) {
			suma+= datos[1][i];
			/*System.out.println("Suma: "+suma);
			System.out.println();*/
			
			if (((i+1) % paso) == 0) { //Cada cinco valores
				double media = suma/paso;
				/*System.out.println("Media: "+media);
				System.out.println();*/
				datosMedios[0][nuevoNumPartidas]=nuevoNumPartidas+1;
				datosMedios[1][nuevoNumPartidas]=media;
				nuevoNumPartidas++;
				suma = 0;
			}
		}
		
		return datosMedios;
	}
	
	public static void graficarMedia(int pasoMedia)
	{
		double[][] datosMedios = hacerMediaDatos(pasoMedia);
		DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Progreso", datosMedios);

        JFreeChart chart = ChartFactory.createXYLineChart("Evolucion del aprendizaje", "Partidas jugadas", "TimeSteps", dataset);

        BufferedImage image = chart.createBufferedImage(600, 400);
        try {
			ImageIO.write(image, "png", new File("evolución del aprendizaje con medias.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("FALLO: La imagen no ha podido ser creada.");
		}
	}
	
	public static void sysoDatos() {
		for (int i = 0; i < datos[0].length; i++) {
			for (int j = 0; j < datos.length; j++) {
				System.out.println(datos[j][i] + " ");
			}
			System.out.println();
		}
	}
	
	/*public static void sysoDatosMedios() {
		double[][] datosMedios = hacerMediaDatos();
		for (int i = 0; i < datosMedios[0].length; i++) {
			for (int j = 0; j < datosMedios.length; j++) {
				System.out.println(datosMedios[j][i] + " ");
			}
			System.out.println();
		}
	}*/
}
