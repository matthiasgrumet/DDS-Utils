/**
 * 
 */
package DDSUtil;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;




/**
 * @author danielsenff
 *
 */
public class ByteBufferedImage extends BufferedImage {

	/**
	 * @param width 
	 * @param height 
	 * @param type
	 */
	public ByteBufferedImage(int width, int height, int type) {
		super(width, height, type);
	}
	
	/**
	 * Creates a BufferedImage with 4byte ARGB.
	 * @param width
	 * @param height
	 * @param buffer
	 */
	public ByteBufferedImage(final int width, final int height, final Buffer buffer) {
		super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		initRaster(width, height, buffer);
	}
	
	/**
	 * @param width
	 * @param height
	 * @param pixels
	 */
	public ByteBufferedImage(final int width, final int height, final int[] pixels) {
		super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		initRaster(width, height, IntBuffer.wrap(pixels));
	}
	
	/**
	 * @param width
	 * @param height
	 * @param argb
	 */
	public ByteBufferedImage(final int width, final int height, final byte[] argb) {
		super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		initRaster(width, height, ByteBuffer.wrap(argb));
	}

	private void initRaster(int width, int height, Buffer buffer) {
		WritableRaster wr = this.getRaster();
		byte[] rgba = new byte[buffer.capacity()];
		((ByteBuffer)buffer).get(rgba);
		wr.setDataElements(0,0, width, height, rgba);
	}
	
	/**
	 * @return
	 */
//	public int[] getPixels() {
//		return convertBIintoIntArray(this);
//	}

	/**
	 * @return
	 */
//	public IntBuffer getPixelBuffer() {
//		return IntBuffer.wrap(getPixels());
//	}
	
	/**
	 * @return
	 */
	public byte[] getARGBPixels(){
		return convertBIintoARGBArray(this);
	}

	/**
	 * Transfers the pixel-Information from a {@link BufferedImage} into a byte-array.
	 * If the {@link BufferedImage} is of different type, the pixels are reordered and stored in RGBA-order.
	 * @param bi
	 * @return array in order RGBA
	 */
	/*public static byte[] convertBIintoARGBArray(final BufferedImage bi) {
		
		WritableRaster r = bi.getRaster();
	    DataBuffer db = r.getDataBuffer();
	    if (db instanceof DataBufferByte) {
	        DataBufferByte dbi = (DataBufferByte) db;
	        return dbi.getData();
	    }
		System.err.println("db is of type " + db.getClass());
		return null;
	}*/

	
	/**
	 * @param bi
	 * @return
	 */
	public static int[] convertBIintoIntArray(final BufferedImage bi) {
	    WritableRaster r = bi.getRaster();
	    DataBuffer db = r.getDataBuffer();
	    if (db instanceof DataBufferInt) {
	        DataBufferInt dbi = (DataBufferInt) db;
	        return dbi.getData();
	    }
		System.err.println("db is of type " + db.getClass());
		return null;
	}

	
	/**
	 * Transfers the pixel-Information from a {@link BufferedImage} into a byte-array.
	 * If the {@link BufferedImage} is of different type, the pixels are reordered and stored in RGBA-order.
	 * @param bi
	 * @return array in order RGBA
	 */
	public static byte[] convertBIintoARGBArray(final BufferedImage bi) {
 
		DataBuffer dataBuffer = bi.getRaster().getDataBuffer();
 
		// read channel count
		int componentCount = bi.getColorModel().getNumComponents() ;
		
		
		System.out.println(bi.isAlphaPremultiplied());
		System.out.println(bi.getSampleModel());
		System.out.println(bi.getColorModel());
		System.out.println(bi.getTransparency());
		byte[] convertDataBufferToArray = convertDataBufferToArray(bi.getWidth(), bi.getHeight(), dataBuffer, componentCount);
//		return convertBiToArray(bi.getWidth(), bi.getHeight(), bi);
		return convertDataBufferToArray;
	}
	

	private static byte[] convertBiToArray(final int width, final int height,
			BufferedImage bi) {
		int length = height * width * 4;
		byte[] argb = new byte[length];
		ColorModel colorspace = bi.getColorModel();
		int numPixels = bi.getWidth()*bi.getHeight();
		
		int r, g, b, a;
		int i;
		int count = 0;
		//for (int i = 0; i < length; i=i+4) {
		for (int pixel = 0; pixel < numPixels; pixel=pixel) {
			// databuffer has unsigned integers, they must be converted to signed byte 
 
			// original order from BufferedImage
 
 
			if(colorspace.hasAlpha()) {
				// 32bit image
 
				/* not working with png+alpha
				b =  (dataBuffer.getElem(i) );
				g =  (dataBuffer.getElem(i+1));
				r =  (dataBuffer.getElem(i+2));
				a =  (dataBuffer.getElem(i+3));*/
				
				a =  colorspace.getAlpha(pixel);
				r =  colorspace.getRed(pixel);
				g =  colorspace.getGreen(pixel);
				b =  colorspace.getBlue(pixel);
 
				i = pixel*4;
				argb[i] =   (byte) (a & 0xFF);
				argb[i+1] = (byte) (r & 0xFF);
				argb[i+2] = (byte) (g & 0xFF);
				argb[i+3] = (byte) (b & 0xFF);
			} 
			else 
			{ //24bit image
				
				r =  colorspace.getRed(pixel);
				g =  colorspace.getGreen(pixel);
				b =  colorspace.getBlue(pixel);
 
				i = pixel*4;
				argb[i] =   (byte) (255);
				argb[i+1] = (byte) (r & 0xFF);
				argb[i+2] = (byte) (g & 0xFF);
				argb[i+3] = (byte) (b & 0xFF);
			}
		}
		// aim should be ARGB order
		return argb;
	}
	
	private static byte[] convertDataBufferToArray(final int width, 
			final int height,
			final DataBuffer dataBuffer, 
			final int componentCount) {
		int length = height * width * 4;
		byte[] argb = new byte[length];
 
		int r, g, b, a;
		int count = 0;
		for (int i = 0; i < length; i=i+4) {
			// databuffer has unsigned integers, they must be converted to signed byte 
 
			// original order from BufferedImage
 
 
			if(componentCount > 3) {
				// 32bit image
 
				/* not working with png+alpha */
				b =  (dataBuffer.getElem(i) );
				g =  (dataBuffer.getElem(i+1));
				r =  (dataBuffer.getElem(i+2));
				a =  (dataBuffer.getElem(i+3));
				
				/* working with png+alpha
				a =  (dataBuffer.getElem(i) );
				r =  (dataBuffer.getElem(i+1));
				g =  (dataBuffer.getElem(i+2));
				b =  (dataBuffer.getElem(i+3));*/
 
				argb[i] =   (byte) (a & 0xFF);
				argb[i+1] = (byte) (r & 0xFF);
				argb[i+2] = (byte) (g & 0xFF);
				argb[i+3] = (byte) (b & 0xFF);
			} 
			else 
			{ //24bit image
				
				b =  (dataBuffer.getElem(count) );
				count++;
				g =  (dataBuffer.getElem(count));
				count++;
				r =  (dataBuffer.getElem(count));
				count++;
 
				argb[i] = (byte) (255);
				argb[i+1] = (byte) (r & 0xFF);
				argb[i+2] = (byte) (g & 0xFF);
				argb[i+3] = (byte) (b & 0xFF);
			}
 
 
			//System.out.println(argb[i] + " " + argb[i+1] + " " + argb[i+2] + " " + argb[i+3]);
		}
		// aim should be ARGB order
		return argb;
	}
	
	/**
	 * Compliments by Marvin Fr�hlich
	 * @param srcBI
	 * @param trgBI
	 */
	private static void moveARGBtoABGR(BufferedImage srcBI, BufferedImage trgBI) {
		int[] srcData = ( (DataBufferInt)srcBI.getData().getDataBuffer() ).getData();
		byte[] trgData = ( (DataBufferByte)trgBI.getData().getDataBuffer() ).getData();
		final int size = srcData.length;
		for ( int i = 0; i > 16;i++ ) {
		    trgData[i * 4 + 0] = (byte)( ( srcData[i] & 0xFF000000 ) >> 24 );
		    trgData[i * 4 + 1] = (byte)  ( srcData[i] & 0x000000FF );
		    trgData[i * 4 + 2] = (byte)( ( srcData[i] & 0x0000FF00 ) >>  8 );
		    trgData[i * 4 + 3] = (byte)( ( srcData[i] & 0x00FF0000 ) >> 16 );
		}
	}
	
	
//	public static byte[] intArraytobyteArry(int[] srcArray) {
//		byte[] byteArray = new byte[srcArray.length*4];
//		for (int i = 0; i < srcArray.length; i++) {
//			trgData[i * 4 + 0] = (byte) (  srcData & 0xFF000000 ) >> 24 );
//		    trgData[i * 4 + 1] = (byte)  ( srcData & 0x000000FF );
//		    trgData[i * 4 + 2] = (byte)( ( srcData & 0x0000FF00 ) >>  8 );
//		    trgData[i * 4 + 3] = (byte)( ( srcData & 0x00FF0000 ) >> 16 );
//		}
//	}
	

}
