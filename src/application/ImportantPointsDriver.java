/**
 * @author 22ddowlin
 * Creates a program which displays most imporant points of a file
 */


package application;
	
import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.Point;
import javafx.scene.shape.Polyline;


public class ImportantPointsDriver extends Application
{
	private ComboBox<Object> cb;
	private Polyline line, original;
	private LinkedList points, originalpoints;
	private VBox root;
	private Slider slider;
	private TextField field;
	private HBox images;
	
	public void start(Stage primaryStage) throws Exception
	{
		points = new LinkedList();
		cb = new ComboBox<Object>();
		cb.getItems().addAll("Swirl", "Octopus", "Butterfly", "Bone", "Apple");
		cb.setValue("Swirl");
		line = new Polyline();
		line.setStroke(Color.RED);
		original = new Polyline();
		points = new LinkedList();
		originalpoints = new LinkedList();
		slider = new Slider(0, 100, 100);
		slider.setMinWidth(1000);
		slider.setMajorTickUnit(10);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		images = new HBox();
		
		field = new TextField();
		field.setTooltip(new Tooltip("Enter the amount of important points"));
		field.setMaxWidth(100);
		
		cb.setOnAction(this::processComboBox);

		field.setOnKeyPressed(new EventHandler<KeyEvent>() 
		{
		    public void handle(KeyEvent ke) 
		    {
		        if (ke.getCode().equals(KeyCode.ENTER)) 
		        {
		        	slider.setValue(Double.parseDouble(field.getText()));
		        	processSliderRelease(null);
		        }
		    }
		});
		
		slider.setOnMouseReleased(this::processSliderRelease);
				
		root = new VBox(cb, slider, field, images);;
		
		scanPointsFromFile("Swirl");

		Scene scene = new Scene(root, 1280, 720);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Important Points");
		primaryStage.show();
	}
	
	/**
	 * Scan points from text and appends .txt
	 * @param text
	 */
	public void scanPointsFromFile(String text)
	{
		try {
		slider.setValue(100);
		field.clear();
		
		line.getPoints().clear();
		original.getPoints().clear();
		
		images.getChildren().remove(original);
		images.getChildren().remove(line);
		
		points.clear();
		originalpoints.clear();
		Scanner sc = new Scanner(new File(text + ".txt"));
		while (sc.hasNext())
		{
			String[] line = sc.nextLine().split(" ");
			Point p = new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
			points.insertBack(p);
			originalpoints.insertBack(p);
		} 
		drawOriginal(originalpoints);
		drawNew(points);
		sc.close();
		} catch (FileNotFoundException e) {}

	}
	
	/**
	 * Scans points from file when combobox is selected
	 * @param event
	 */
	public void processComboBox(ActionEvent event)
	{
		scanPointsFromFile((String) cb.getValue());
	}
	
	/**
	 * When slider released, adjusts points accordingly
	 * @param event
	 */
	public void processSliderRelease(MouseEvent event)
	{
		field.setText(Integer.toString((int)slider.getValue()));
		line.getPoints().clear();
		images.getChildren().remove(line);
		LinkedList ips = ImportantPoint.ImportantPoints(points, (int)slider.getValue());
		drawNew(ips);
	}
	
	/**
	 * Draws new image based in points
	 * @param points
	 */
	public void drawNew(LinkedList points)
	{
		Node current = points.getTheHeadNode();
		while (current!=null)
		{
			Point p = (Point)current.getData();
			line.getPoints().addAll((double) p.x, (double) p.y);;
			current = current.getNext();
		}
		current = points.getTheHeadNode();
		Point p = (Point)current.getData();
		line.getPoints().addAll((double)p.x,(double)p.y);
		images.getChildren().add(line);
	}
	
	/**
	 * Draws original image based on points
	 * @param points
	 */
	public void drawOriginal(LinkedList points)
	{
		Node current = points.getTheHeadNode();
		while (current!=null)
		{
			Point p = (Point)current.getData();
			original.getPoints().addAll((double) p.x, (double) p.y);;
			current = current.getNext();
		}
		current = points.getTheHeadNode();
		Point p = (Point)current.getData();
		original.getPoints().addAll((double)p.x,(double)p.y);
		images.getChildren().add(original);
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
