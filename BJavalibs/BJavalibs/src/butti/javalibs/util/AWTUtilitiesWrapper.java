package butti.javalibs.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Source: http://java.sun.com/developer/technicalArticles/GUI/
 * translucent_shaped_windows/
 * 
 * @author Anthony Petrov
 */
public class AWTUtilitiesWrapper {

	private static Class<?> awtUtilitiesClass;
	private static Class<?> translucencyClass;
	private static Method mIsTranslucencySupported, mIsTranslucencyCapable, mSetWindowShape, mSetWindowOpacity, mSetWindowOpaque;
	public static Object PERPIXEL_TRANSPARENT, TRANSLUCENT, PERPIXEL_TRANSLUCENT;

	static void init() {
		try {
			awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
			translucencyClass = Class.forName("com.sun.awt.AWTUtilities$Translucency");
			if (translucencyClass.isEnum()) {
				Object[] kinds = translucencyClass.getEnumConstants();
				if (kinds != null) {
					PERPIXEL_TRANSPARENT = kinds[0];
					TRANSLUCENT = kinds[1];
					PERPIXEL_TRANSLUCENT = kinds[2];
				}
			}
			mIsTranslucencySupported = awtUtilitiesClass.getMethod("isTranslucencySupported", translucencyClass);
			mIsTranslucencyCapable = awtUtilitiesClass.getMethod("isTranslucencyCapable", GraphicsConfiguration.class);
			mSetWindowShape = awtUtilitiesClass.getMethod("setWindowShape", Window.class, Shape.class);
			mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
			mSetWindowOpaque = awtUtilitiesClass.getMethod("setWindowOpaque", Window.class, boolean.class);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	static {
		init();
	}

	private static boolean isSupported(Method method, Object kind) {
		if (awtUtilitiesClass == null || method == null) {
			return false;
		}
		try {
			Object ret = method.invoke(null, kind);
			if (ret instanceof Boolean) {
				return ((Boolean) ret).booleanValue();
			}
		} catch (IllegalAccessException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public static boolean isTranslucencySupported(Object kind) {
		if (translucencyClass == null) {
			return false;
		}
		return isSupported(mIsTranslucencySupported, kind);
	}

	public static boolean isTranslucencyCapable(GraphicsConfiguration gc) {
		return isSupported(mIsTranslucencyCapable, gc);
	}

	private static void set(Method method, Window window, Object value) {
		if (awtUtilitiesClass == null || method == null) {
			return;
		}
		try {
			method.invoke(null, window, value);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void setWindowShape(Window window, Shape shape) {
		set(mSetWindowShape, window, shape);
	}

	public static void setWindowOpacity(Window window, float opacity) {
		set(mSetWindowOpacity, window, Float.valueOf(opacity));
	}

	public static void setWindowOpaque(Window window, boolean opaque) {
		set(mSetWindowOpaque, window, Boolean.valueOf(opaque));
	}

	public static GraphicsConfiguration getTranslucencyCapableGC() {
		GraphicsConfiguration translucencyCapableGC = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		if (!AWTUtilitiesWrapper.isTranslucencyCapable(translucencyCapableGC)) {
			translucencyCapableGC = null;

			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();

			for (int i = 0; i < devices.length && translucencyCapableGC == null; i++) {
				GraphicsConfiguration[] configs = devices[i].getConfigurations();
				for (int j = 0; j < configs.length && translucencyCapableGC == null; j++) {
					if (AWTUtilitiesWrapper.isTranslucencyCapable(configs[j])) {
						translucencyCapableGC = configs[j];
					}
				}
			}
		}
		return translucencyCapableGC;
	}
}