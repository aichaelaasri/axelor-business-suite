/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2014 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.base.service.imports.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.base.exceptions.IExceptionMessage;
import com.axelor.data.Listener;
import com.axelor.db.Model;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.IException;
import com.axelor.exception.service.TraceBackService;
import com.axelor.i18n.I18n;

public class ImporterListener implements Listener {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private String name, importLog = "";
	private int totalRecord, successRecord, notNull, anomaly;
	
	public ImporterListener( String name ){ 
		this.name = name;
		this.totalRecord = this.successRecord = this.notNull = this.anomaly = 0;
	}
	
	public String getImportLog(){
		
		String log = importLog;
		log += I18n.get(IExceptionMessage.IMPORTER_LISTERNER_1)+ totalRecord + I18n.get(IExceptionMessage.IMPORTER_LISTERNER_2)  + successRecord + " - Non null : " + notNull;
		log += I18n.get(IExceptionMessage.IMPORTER_LISTERNER_3) + anomaly;
		
		return log;
	}

	@Override
	public void imported(Model bean) { if( bean != null ) { notNull++; } }

	@Override
	public void imported(Integer total, Integer success) {
		totalRecord = total; successRecord = success;
	}

	@Override
	public void handle(Model bean, Exception e) {
		anomaly++;
		importLog += "\n"+e;
		TraceBackService.trace( new AxelorException (
				String.format(I18n.get(IExceptionMessage.IMPORTER_LISTERNER_4), name ), 
				e, IException.FUNCTIONNAL )
		, IException.IMPORT );
	}

}
