/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import stanbic.stanbicmob.com.stanbicagent.R;
import model.GetAgentRequests;
import model.GetBanksData;

public class RequestAdapt extends ArrayAdapter<GetAgentRequests> implements Filterable {

	private List<GetAgentRequests> planetList;
	private Context context;
	private Filter planetFilter;
	private List<GetAgentRequests> origPlanetList;

	public RequestAdapt(List<GetAgentRequests> planetList, Context ctx) {
		super(ctx, R.layout.ben_list, planetList);
		this.planetList = planetList;
		this.context = ctx;
		this.origPlanetList = planetList;
	}
	
	public int getCount() {
		return planetList.size();
	}

	public GetAgentRequests getItem(int position) {
		return planetList.get(position);
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.agentreqlist, null);
			// Now we can fill the layout with the right values
			TextView reason = (TextView) v.findViewById(R.id.reason);
			TextView statuss = (TextView) v.findViewById(R.id.statuss);
			TextView reqdate = (TextView) v.findViewById(R.id.reqdate);

			

			
			holder.reason = reason;
			holder.status = statuss;
			holder.reqdate = reqdate;
			
			v.setTag(holder);
		}
		else 
			holder = (PlanetHolder) v.getTag();

		GetAgentRequests p = planetList.get(position);

		holder.reason.setText(p.getreason());
		String txtst = "";
		if(p.getstatus().equals("0")){
			txtst = "Pending";
		}
		if(p.getstatus().equals("1")){
			txtst = "Bank Responded";
		}
		if(p.getstatus().equals("3")){
			txtst = "Closed";
		}
		holder.status.setText(txtst);
		holder.reqdate.setText(p.getrequestedVisitDate());


		
		
		return v;
	}

	public void resetData() {
		planetList = origPlanetList;
	}
	
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class PlanetHolder {
		public TextView reqdate;
		public TextView reason;
		public TextView status;

	}
	

	


}
