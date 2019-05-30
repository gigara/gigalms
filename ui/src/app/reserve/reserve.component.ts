import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { backend_url } from '../global';

@Component({
  selector: 'app-form',
  templateUrl: './reserve.component.html',
  styleUrls: ['./reserve.component.css']
})
export class ReserveComponent implements OnInit {
  

  borrowForm: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder,private http: HttpClient) { }

  private onSubmit() {
    this.submitted = true;

    //post request body
    let body = new URLSearchParams();
    body.append('isbn', this.borrowForm.value.isbn);
    body.append('reader', this.borrowForm.value.reader);

    //set post sending type
    let header = {headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')};
   
    //send data to backend
    this.http.post(backend_url+'/item/reserve',body.toString(),header).subscribe(
        response => {
            let httpResponse = JSON.parse(JSON.stringify(response));
            alert(httpResponse.status);
        },
        error => {
            console.log("Error", error);
        }
    ); 

}

  ngOnInit() {
      this.borrowForm = this.formBuilder.group({
          isbn: ['', [Validators.required, Validators.pattern("^[0-9]*$")]],
          reader: ['', [Validators.required, Validators.pattern("^[0-9]*$")]],
          date: ['', Validators.required],
          time: ['', Validators.required]
      });
  }

  // convenience getter for easy access to form fields
  get f() { return this.borrowForm.controls; }

}
