package rasterunit;

import java.io.IOException;
import java.util.Collection;
import java.util.NavigableSet;

import util.Range2d;
import util.collections.ReadonlyNavigableSetView;

public interface RasterUnitStorage extends AutoCloseable {
	Range2d getTileRange();
	ReadonlyNavigableSetView<TileKey> tileKeysReadonly();
	ReadonlyNavigableSetView<BandKey> bandKeysReadonly();
	ReadonlyNavigableSetView<Integer> timeKeysReadonly();
	boolean isEmpty();
	
	Tile readTile(TileKey tileKey) throws IOException;
	Tile readTile(int t, int b, int y, int x) throws IOException;
	Collection<Tile> readTiles(int t, int b, int ymin, int ymax, int xmin, int xmax);
	NavigableSet<TileKey> getTileKeys(int t, int b, int y, int xmin, int xmax);
	Collection<Tile> getTiles(TileKey keyXmin, TileKey keyXmax);
	NavigableSet<RowKey> getRowKeys(int t, int b, int ymin, int ymax);
	Range2d getTileRange(BandKey bandKey);
	
	void writeTile(Tile tile) throws IOException;
	
	void commit();
	void flush() throws IOException;
	void close() throws IOException;
	int getTileCount();
	
	default NavigableSet<RowKey> getRowKeysReverse(int t, int b, int ymin, int ymax) {
		return getRowKeys(t, b, ymin, ymax).descendingSet();
	}
	
	default Collection<Tile> getTilesYReverse(int t, int b, int ymin, int ymax, int xmin, int xmax) {
		Collection<RowKey> rows = getRowKeysReverse(t, b, ymin, ymax);
		return new TileCollection(this, rows, xmin, xmax);
	}
	long removeAllTilesOfTimestamp(int t) throws IOException;
}
