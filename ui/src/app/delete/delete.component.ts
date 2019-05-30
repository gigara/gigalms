import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { backend_url } from '../global';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.css']
})
export class DeleteComponent implements OnInit {

  deleteItem: FormGroup;

  constructor(private formBuilder: FormBuilder,private http: HttpClient) { }

  onSubmit(){

    //set request header
    let header = {headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')};

    //send delete request to the backend
    this.http.delete(backend_url+'/item/delete/'+this.deleteItem.value.isbn,header)
    .subscribe(
      response => {
        
          let httpResponse = JSON.parse(JSON.stringify(response));
          //show the status
          alert(httpResponse.status);

          //show available spaces
          if(httpResponse.Books != null){
            alert("Spaces availble\nBooks: "+httpResponse.Books+"\nDVDs: "+httpResponse.DVDs);
          }
      },
      error => {
          console.log("Error", error);
      }
  );

  }

  ngOnInit() {
    this.deleteItem = this.formBuilder.group({
      isbn: ['', [Validators.required, Validators.pattern("^[0-9]*$")]]
  });
  }

  
}
