package remotetask.rasterdb;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import broker.Broker;
import broker.acl.EmptyACL;
import rasterdb.Band;
import rasterdb.RasterDB;
import rasterdb.RasterDBimporter;
import remotetask.Context;
import remotetask.Description;
import remotetask.Param;
import remotetask.RemoteTask;

@task_rasterdb("import")
@Description("import raster file data into rasterdb layer")
@Param(name="rasterdb", desc="ID of RasterDB layer (new or existing)")
@Param(name="file", desc="Raster file to import (located on server)")
@Param(name="band", desc="existing band number as import target", required=false)
public class Task_import extends RemoteTask {
	private static final Logger log = LogManager.getLogger();

	private final Broker broker;
	private final JSONObject task;

	public Task_import(Context ctx) {
		this.broker = ctx.broker;
		this.task = ctx.task;
		EmptyACL.ADMIN.check(ctx.userIdentity);
	}

	@Override
	public void process() {
		String name = task.getString("rasterdb");
		RasterDB rasterdb =  broker.createOrGetRasterdb(name);
		String filename = task.getString("file");

		Band band = null;
		if(task.has("band")) {
			int band_number = task.getInt("band");
			band = rasterdb.getBandByNumber(band_number);
			if(band == null) {
				throw new RuntimeException("band number not found "+band_number);
			}
		}

		RasterDBimporter importer = new RasterDBimporter(rasterdb);

		try {
			importer.importFile_GDAL(Paths.get(filename), band, false, 0);
		} catch (IOException e) {
			log.error(e);
		}
	}
}