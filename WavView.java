import java.awt.Color;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {
	
	short[] buffer;// data buffer
	int skip = 1024;// sampling data
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
		g.setColor(Color.RED);
		for (int i = 0; i < buffer.length -skip; i+=skip){// data sampling
			int data = getDatas(buffer[i]), data2 = getDatas(buffer[i+skip]);
			if (data == 0)//Data correction
				data = getHeight()/2;
			if (data2 == 0)
				data2 = getHeight()/2;
			g.drawLine(getWidth()*i / (buffer.length -1), data,
					getWidth()*(i+skip) / (buffer.length -1), data2);
		}
		g.setColor(Color.BLUE);
		for (int i = 1; i < buffer.length -skip; i+=skip){
			int data = getDatas(buffer[i]), data2 = getDatas(buffer[i+skip]);
			if (data == 0)//Data correction
				data = getHeight()/2;
			if (data2 == 0)
				data2 = getHeight()/2;
			g.drawLine(getWidth()*i / (buffer.length -1), data,
					getWidth()*(i+skip) / (buffer.length -1), data2);
		}
	}
	
	/**
	 * calculate data
	 * @param data
	 * @return
	 */
	int getDatas(short data){
		int size = getHeight()/2;
		if (data >= 0){
	        return data*size/32767; // Positive data   (32767[16bit data] : data = size : x)
		}else 
	        return ((-data)*size/32767) + size;/// negative data
	}


	/**
	 * File Options
	 * Name : Test1.wav
	 * 48000Hz 2Ch 16bit
	 * @param args
	 */
	public static void main(String args[]) {

		Main panel = new Main();
		JFrame app = new JFrame("Draw");
		app.add(panel);
		app.setSize(1500, 400);
		String f = "Test1.wav";//File name
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File("C:/Users/RyuSoungJin/Desktop/" + f)));
			stream.skip(44 + skip_point);//Head + skipPoint(0)
			byte[] buff = new byte[192000];//getBuffer  (48000 * 2[ch] * 2[bytes])    // 2bytes == 16bits
			stream.read(buff);//readBuffer
			panel.buffer = getData(buff);// put data
			stream.close();//stream close
		} catch (IOException e) {
			e.printStackTrace();
		}
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}
	
	static short[] getData(byte arr[]){
        short arrs[] = new short[arr.length/2];
        for (int i = 0; i < arr.length; i+=2)
            arrs[i/2] = get2Data(arr, i, i+2);
        return arrs;
    }

    static short get2Data(byte arr[], int start, int end){
        byte data[] = new byte[end - start];
        for (int i = 0; i < data.length; i++){
            data[i] = arr[start + i];
        }
        return get2Data(data);
    }

    static short get2Data(byte arr[]){
        return (short) (((0xff & arr[1]) << 8) |
                ((0xff & arr[0])));
    }
}
