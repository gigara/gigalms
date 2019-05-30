import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatSortable, MatTableDataSource } from '@angular/material';
import { HttpClient } from '@angular/common/http';

import { backend_url } from '../../global';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  @ViewChild(MatSort) sort: MatSort;

  dataSource;
  displayedColumns = ['isbn','title','reader','borrowedDate','fine']; //table headers
 
  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.http.get(backend_url+'/item/report').subscribe(results => {
      if(!results){
        return;
      }
      this.dataSource = new MatTableDataSource(results['all']); // set datasource
      this.dataSource.sort = this.sort;
    }) 
  }

}
