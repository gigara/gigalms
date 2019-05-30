import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { backend_url } from '../../global';

@Component({
  selector: 'app-return-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class ReturnFormComponent implements OnInit {

  
  returnForm: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder,private http: HttpClient) { }

  private onSubmit() {
    this.submitted = true;

    //post request body
    let body = new URLSearchParams();
    body.append('isbn', this.returnForm.value.isbn);
    body.append('date', this.returnForm.value.date.split('-')[2]);
    body.append('month', this.returnForm.value.date.split('-')[1]);
    body.append('year', this.returnForm.value.date.split('-')[0]);
    body.append('minute', this.returnForm.value.time.split(':')[0]);
    body.append('hour', this.returnForm.value.time.split(':')[1]);

    //set post sending type
    let header = {headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')};
   
    //send data to backend
    this.http.post(backend_url+'/item/return',body.toString(),header).subscribe(
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
    this.returnForm = this.formBuilder.group({
        isbn: ['', [Validators.required, Validators.pattern("^[0-9]*$")]],
        date: ['', Validators.required],
        time: ['', Validators.required]
    });
}

// convenience getter for easy access to form fields
get f() { return this.returnForm.controls; }

}
