import com.healthmarketscience.jackcess.Row;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;

public class Main extends Application {
    private static double ADDRESS_X = 465.0D;
    private static double ADDRESS_Y = 395.0D;
    private static final double DEFAULT_HEIGHT = 637.0D;
    private static final double DEFAULT_FONT_SIZE = 26.0D;
    private static File FRONT_IMG;
    private static File BACK_IMG;
    private static File DB_FILE;
    private static File DEST_DIR = new File(System.getProperty("user.home") + "/Desktop/");
    private static final String ADDRESS_LINE_1 = "Aris Santas";
    private static final String ADDRESS_LINE_2 = "2705 Dogwood Circle";
    private static final String ADDRESS_LINE_3 = "Valdosta, GA 31602";
    private static boolean GENERATING_IMAGES;

    public Main() {
    }

    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        Pane addressPane = new Pane();
        ImageView imageView = new ImageView();
        FileChooser frontImgChooser = new FileChooser();
        FileChooser backImgChooser = new FileChooser();
        FileChooser dbChooser = new FileChooser();
        ExtensionFilter pngFilter = new ExtensionFilter("PNG Images (*.png)", new String[]{"*.png"});
        ExtensionFilter mdbFilter = new ExtensionFilter("MS Access Database (*.mdb)", new String[]{"*.mdb"});
        DirectoryChooser destChooser = new DirectoryChooser();
        File imageDir = new File("../Packages & Specials/");
        File dbDir = new File("../Intakes and Evals/");
        Text frontImgText = new Text("Front Image");
        Text backImgText = new Text("Back Image");
        Text dbText = new Text("Database");
        Text exportText = new Text("Destination");
        TextField frontImgPath = new TextField("select address side");
        TextField backImgPath = new TextField("select coupon side");
        TextField dbPath = new TextField("select database");
        TextField destPath = new TextField(DEST_DIR.getPath());
        Button frontImgBtn = new Button("...");
        Button backImgBtn = new Button("...");
        Button dbBtn = new Button("...");
        Button exportBrowseBtn = new Button("...");
        Button exportBtn = new Button("Export Images");
        grid.setHgap(10.0D);
        grid.setVgap(5.0D);
        grid.setPadding(new Insets(10.0D));
        frontImgChooser.getExtensionFilters().add(pngFilter);
        backImgChooser.getExtensionFilters().add(pngFilter);
        dbChooser.getExtensionFilters().add(mdbFilter);
        if(imageDir.exists()) {
            frontImgChooser.setInitialDirectory(imageDir);
            backImgChooser.setInitialDirectory(imageDir);
        }

        if(dbDir.exists()) {
            dbChooser.setInitialDirectory(dbDir);
        }

        destChooser.setInitialDirectory(DEST_DIR);
        imageView.setFitHeight(637.0D);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        frontImgPath.setEditable(false);
        backImgPath.setEditable(false);
        dbPath.setEditable(false);
        destPath.setEditable(false);
        frontImgBtn.setOnAction((event) -> {
            if(!GENERATING_IMAGES) {
                File newImg = frontImgChooser.showOpenDialog(primaryStage);
                FRONT_IMG = newImg == null?FRONT_IMG:newImg;
                frontImgPath.setText(FRONT_IMG.getPath());
                repaintAddress(imageView);
            }

        });
        backImgBtn.setOnAction((event) -> {
            if(!GENERATING_IMAGES) {
                File newImg = backImgChooser.showOpenDialog(primaryStage);
                BACK_IMG = newImg == null?BACK_IMG:newImg;
                backImgPath.setText(BACK_IMG.getPath());
            }

        });
        dbBtn.setOnAction((event) -> {
            if(!GENERATING_IMAGES) {
                File newDb = dbChooser.showOpenDialog(primaryStage);
                DB_FILE = newDb == null?DB_FILE:newDb;
                dbPath.setText(DB_FILE.getPath());
            }

        });
        exportBrowseBtn.setOnAction((event) -> {
            if(!GENERATING_IMAGES) {
                File newDir = destChooser.showDialog(primaryStage);
                DEST_DIR = newDir == null?DEST_DIR:newDir;
                destPath.setText(DEST_DIR.getPath());
            }

        });
        addressPane.setOnMouseClicked((event) -> {
            if(!GENERATING_IMAGES && FRONT_IMG != null) {
                ADDRESS_X = event.getX() - 20.0D;
                ADDRESS_Y = event.getY() - 20.0D;
                repaintAddress(imageView);
            }

        });
        exportBtn.setOnAction((event) -> {
            if(!GENERATING_IMAGES) {
                generateImages(grid);
            }

        });
        addressPane.setBlendMode(BlendMode.DARKEN);
        grid.add(frontImgText, 0, 0);
        grid.add(frontImgPath, 0, 1);
        grid.add(frontImgBtn, 1, 1);
        grid.add(backImgText, 0, 2);
        grid.add(backImgPath, 0, 3);
        grid.add(backImgBtn, 1, 3);
        grid.add(dbText, 0, 4);
        grid.add(dbPath, 0, 5);
        grid.add(dbBtn, 1, 5);
        grid.add(exportText, 0, 6);
        grid.add(destPath, 0, 7);
        grid.add(exportBrowseBtn, 1, 7);
        grid.add(exportBtn, 0, 8);
        grid.add(imageView, 2, 0, 50, 50);
        grid.add(addressPane, 2, 0, 50, 50);
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        Scene scene = new Scene(root, 1038.0D, 657.0D);
        primaryStage.setTitle("Mindful Massage Mailer Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void generateImages(GridPane grid) {
        if(FRONT_IMG != null && BACK_IMG != null && DB_FILE != null && DEST_DIR != null) {
            GENERATING_IMAGES = true;
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(180.0D);
            1 generateImagesTask = new 1(progressBar, grid);
            progressBar.progressProperty().bind(generateImagesTask.progressProperty());
            Thread thread = new Thread(generateImagesTask);
            thread.setDaemon(true);
            thread.start();
            grid.add(progressBar, 0, 9);
        } else {
            (new Alert(AlertType.ERROR, "Please provide input for every field.", new ButtonType[]{ButtonType.OK})).show();
        }

    }

    private static BufferedImage redrawImage(BufferedImage original, Row row) {
        return redrawImage(original, row.getString("FirstName") + " " + row.getString("LastName"), row.getString("Address"), row.getString("City") + ", " + row.getString("State") + " " + row.getString("PostalCode"));
    }

    private static BufferedImage redrawImage(BufferedImage original, String line1, String line2, String line3) {
        int x = (int)(ADDRESS_X * (double)original.getHeight() / 637.0D);
        int y = (int)(ADDRESS_Y * (double)original.getHeight() / 637.0D);
        Font font = new Font("Arial Regular", 0, (int)(26.0D * (double)original.getHeight() / 637.0D));
        BufferedImage modified = new BufferedImage(original.getWidth(), original.getHeight(), 2);
        Graphics2D graphics = modified.createGraphics();
        graphics.drawImage(original, 0, 0, (ImageObserver)null);
        graphics.setPaint(Color.black);
        graphics.setFont(font);
        graphics.drawString(line1, x, y);
        graphics.drawString(line2 == null?"":line2, x, (int)((double)y + (double)font.getSize() * 1.2D));
        graphics.drawString(line3, x, (int)((double)y + (double)font.getSize() * 2.4D));
        graphics.dispose();
        return modified;
    }

    private static void repaintAddress(ImageView imageView) {
        File tempImgFile = new File("tempImg.png");

        try {
            BufferedImage tempBufferedImg = ImageIO.read(FRONT_IMG);
            ImageIO.write(redrawImage(tempBufferedImg, "Aris Santas", "2705 Dogwood Circle", "Valdosta, GA 31602"), "PNG", tempImgFile);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        try {
            imageView.setImage(new Image(tempImgFile.toURI().toURL().toExternalForm()));
        } catch (MalformedURLException var4) {
            (new Alert(AlertType.ERROR, "Problem locating image.", new ButtonType[]{ButtonType.OK})).show();
        }

    }
}
