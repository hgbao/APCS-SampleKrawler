import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
	private static String urlKhaiVi="http://giavien.vn/canh---lau-pc,8701";

	public static void main(String[] args) {
		String content = "";
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

		driver.get(urlKhaiVi);
		List<WebElement> foodBoxes = driver.findElements(By.className("item-product"));
		System.out.println("Number of foods: " + foodBoxes.size());
		
		for (int i = 0; i < foodBoxes.size(); i++){
			WebElement foodBox = foodBoxes.get(i);
			try{
				//Text data
				WebElement webName  = foodBox.findElements(By.xpath("//div[@class='box-info-product']//h4//a")).get(i);
				String foodName = webName.getText();
				System.out.println(i + "/" + foodBoxes.size() + ": " + foodName);
				WebElement webPrice = foodBox.findElements(By.xpath("//span[@class='price']")).get(i);
				String foodPrice = webPrice.getText();
				foodPrice = foodPrice.substring(0 , foodPrice.indexOf('.')) + "000";
				
				//Image data
				WebElement webImage = foodBox.findElements(By.xpath("//div[@class='box-img-product']//img")).get(i);
				String imageSRC = webImage.getAttribute("src");
				BufferedImage foodImage = ImageIO.read(new URL(imageSRC));
				
				String imageName = imageSRC.substring(imageSRC.lastIndexOf("/") + 1);
				File imageFile = new File("image/" + imageName);
				if (!imageFile.exists())
					imageFile.createNewFile();

				ImageIO.write(foodImage, "png", imageFile);
				
				//Save data
				content += "INSERT INTO 'foods' VALUES";
				content += "('" + (i + 234) + "', '" + foodName + "','" + foodName + "',";
				content += "'" + foodPrice + "','','','available','" + imageName + "','image/png','" + imageFile.length() + "',";
				content += "null, '8', '12', '---:price: 0.0\npercentage: 0\n:status: false'\n);";
				content += "\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		try{
			PrintWriter out = new PrintWriter(new FileWriter("output.txt", true), true);
			out.write(content);
			out.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		driver.close();
		driver.quit();
	}

}
