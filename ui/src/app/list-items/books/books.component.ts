import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatSortable, MatTableDataSource } from '@angular/material';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddBookComponent } from './add/add.component';
import { backend_url } from '../../global';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
  @ViewChild(MatSort) sort: MatSort;
  
  dataSource;
  displayedColumns = ['isbn','title','author','publisher','publicationDate','pages','availability']; //table headers
  
  constructor(private http: HttpClient,private modalService: NgbModal) { }

 
  openFormModal() {
    const modalRef = this.modalService.open(AddBookComponent);

    modalRef.result.then((result) => {
      //console.log(result);
    }).catch((error) => {
      console.log(error);
    });
  }

  getBook(): Observable<Book[]>{                              // get data from the backend
    return this.http.get<Book[]>(backend_url+"/item/list");
  }

  ngOnInit() {
    //get data from the backend
    this.getBook().subscribe(results => {
      if(!results){
        return;
      }
      this.dataSource = new MatTableDataSource(results["Books"]); // set datasource
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
