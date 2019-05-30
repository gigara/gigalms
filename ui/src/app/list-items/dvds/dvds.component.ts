import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatSortable, MatTableDataSource } from '@angular/material';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
//import { DVD } from '../../models/dvd.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddDVDComponent } from './add/add.component';
import { backend_url } from '../../global';

@Component({
  selector: 'app-dvds',
  templateUrl: './dvds.component.html',
  styleUrls: ['./dvds.component.css']
})
export class DVDsComponent implements OnInit {
  @ViewChild(MatSort) sort: MatSort;
  
  dataSource;
  displayedColumns = ['isbn','title','sector','producer','publishDate','actors','language','subtitles','availability']; //table headers
  private serviceUrl = backend_url+'/item/list';         // json Url from backend

  constructor(private http: HttpClient,private modalService: NgbModal) { }

  getDVD(): Observable<DVD[]>{                              // get data from the backend
    return this.http.get<DVD[]>(this.serviceUrl);
  }

  openFormModal() {
    const modalRef = this.modalService.open(AddDVDComponent);

    modalRef.result.then((result) => {
      console.log(result);
    }).catch((error) => {
      console.log(error);
    });
  }

  ngOnInit() {
    this.getDVD().subscribe(results => {
      if(!results){
        return;
      }
      this.dataSource = new MatTableDataSource(results["DVDs"]); // set datasource
      this.dataSource.sort = this.sort;
    }) 
  }

  //search item
  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  //check availability
  isAvailable(borrowDetails: any){
    if(borrowDetails[0] != undefined){
      if((borrowDetails[borrowDetails.length - 1].returnedTime.date) == 0){
        return false;
      }
    }
    return true;
  }
}

