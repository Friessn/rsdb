package server.api.voxeldbs;

import util.Range3d;

public class VoxelProcessing {
	
	public static Range3d getRange(byte[][][] data, int vrxlen, int vrylen, int vrzlen) {
		int clxmin = Integer.MAX_VALUE;
		int clymin = Integer.MAX_VALUE;
		int clzmin = Integer.MAX_VALUE;
		int clxmax = Integer.MIN_VALUE;
		int clymax = Integer.MIN_VALUE;
		int clzmax = Integer.MIN_VALUE;	
		for (int z = 0; z < vrzlen; z++) {
			byte[][] rZ = data[z];
			for (int y = 0; y < vrylen; y++) {
				byte[] rZY = rZ[y];
				for (int x = 0; x < vrxlen; x++) {
					byte v = rZY[x];
					if(v != 0) {
						if(x < clxmin) {
							clxmin = x;
						}
						if(clxmax < x) {
							clxmax = x;
						}
						if(y < clymin) {
							clymin = y;
						}
						if(clymax < y) {
							clymax = y;
						}
						if(z < clzmin) {
							clzmin = z;
						}
						if(clzmax < z) {
							clzmax = z;
						}						
					}
				}
			}
		}
		return new Range3d(clxmin, clymin, clzmin, clxmax, clymax, clzmax);
	}
	
	public static Range3d getRange(char[][][] data, int vrxlen, int vrylen, int vrzlen) {
		int clxmin = Integer.MAX_VALUE;
		int clymin = Integer.MAX_VALUE;
		int clzmin = Integer.MAX_VALUE;
		int clxmax = Integer.MIN_VALUE;
		int clymax = Integer.MIN_VALUE;
		int clzmax = Integer.MIN_VALUE;	
		for (int z = 0; z < vrzlen; z++) {
			char[][] rZ = data[z];
			for (int y = 0; y < vrylen; y++) {
				char[] rZY = rZ[y];
				for (int x = 0; x < vrxlen; x++) {
					char v = rZY[x];
					if(v != 0) {
						if(x < clxmin) {
							clxmin = x;
						}
						if(clxmax < x) {
							clxmax = x;
						}
						if(y < clymin) {
							clymin = y;
						}
						if(clymax < y) {
							clymax = y;
						}
						if(z < clzmin) {
							clzmin = z;
						}
						if(clzmax < z) {
							clzmax = z;
						}						
					}
				}
			}
		}
		return new Range3d(clxmin, clymin, clzmin, clxmax, clymax, clzmax);
	}
	
	public static Range3d getRange(int[][][] data, int vrxlen, int vrylen, int vrzlen) {
		int clxmin = Integer.MAX_VALUE;
		int clymin = Integer.MAX_VALUE;
		int clzmin = Integer.MAX_VALUE;
		int clxmax = Integer.MIN_VALUE;
		int clymax = Integer.MIN_VALUE;
		int clzmax = Integer.MIN_VALUE;	
		for (int z = 0; z < vrzlen; z++) {
			int[][] rZ = data[z];
			for (int y = 0; y < vrylen; y++) {
				int[] rZY = rZ[y];
				for (int x = 0; x < vrxlen; x++) {
					int v = rZY[x];
					if(v != 0) {
						if(x < clxmin) {
							clxmin = x;
						}
						if(clxmax < x) {
							clxmax = x;
						}
						if(y < clymin) {
							clymin = y;
						}
						if(clymax < y) {
							clymax = y;
						}
						if(z < clzmin) {
							clzmin = z;
						}
						if(clzmax < z) {
							clzmax = z;
						}						
					}
				}
			}
		}
		return new Range3d(clxmin, clymin, clzmin, clxmax, clymax, clzmax);
	}
	
	public static byte[] toBytes(byte[][][] r, Range3d localRange) {
		byte[] data = new byte[localRange.xyzlen()];

		int pos = 0;
		for (int z = localRange.zmin; z <= localRange.zmax; z++) {
			byte[][] rZ = r[z];
			for (int y = localRange.ymin; y <= localRange.ymax; y++) {
				byte[] rZY = rZ[y];
				for (int x = localRange.xmin; x <= localRange.xmax; x++) {
					byte v = rZY[x];
					data[pos++] = v;
				}
			}
		}
		
		return data;
	}
	
	public static byte[] toBytes(int[][][] r, Range3d localRange) {
		byte[] data = new byte[localRange.xyzlen() * 4];

		int pos = 0;
		for (int z = localRange.zmin; z <= localRange.zmax; z++) {
			int[][] rZ = r[z];
			for (int y = localRange.ymin; y <= localRange.ymax; y++) {
				int[] rZY = rZ[y];
				for (int x = localRange.xmin; x <= localRange.xmax; x++) {
					int v = rZY[x];					
					data[pos++] = (byte)(v >>> 24);
					data[pos++] = (byte)(v >>> 16);
					data[pos++] = (byte)(v >>>  8);
					data[pos++] = (byte)(v >>>  0);
				}
			}
		}
		
		return data;
	}
	
	public static byte[] toBytes(char[][][] r, Range3d localRange) {
		byte[] data = new byte[localRange.xyzlen() * 2];

		int pos = 0;
		for (int z = localRange.zmin; z <= localRange.zmax; z++) {
			char[][] rZ = r[z];
			for (int y = localRange.ymin; y <= localRange.ymax; y++) {
				char[] rZY = rZ[y];
				for (int x = localRange.xmin; x <= localRange.xmax; x++) {
					char v = rZY[x];					
					data[pos++] = (byte)(v >>>  8);
					data[pos++] = (byte)(v >>>  0);
				}
			}
		}
		
		return data;
	}
}
