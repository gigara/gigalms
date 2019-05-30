import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { backend_url } from '../../../global';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddBookComponent implements OnInit {
  addNewBookForm: FormGroup;
  responseMsg: String;
  
  constructor(public activeModal: NgbActiveModal,private formBuilder: FormBuilder,private http:HttpClient) { }

  //popup close button
  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  //form submit button
  submitForm() {
    //post request body
    let body = new URLSearchParams();
    body.append('isbn', this.addNewBookForm.value.isbn);
    body.append('title', this.addNewBookForm.value.title);
    body.append('sector', this.addNewBookForm.value.sector);
    body.append('publisher', this.addNewBookForm.value.publisher);
    body.append('pubDate', this.addNewBookForm.value.publisheDate);
    body.append('pages', this.addNewBookForm.value.noOfPages);

    for(let i=0; i < this.addNewBookForm.value.authors.length; i++){
        body.append('author', this.addNewBookForm.value.authors[i].author);
    
    }

    //set post sending type
    let header = {headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')};

    //hide the form and display loading
    (document.querySelector('form') as HTMLElement).className = 'transform-bounce';
    setTimeout( () => { (document.querySelector('form') as HTMLElement).className = 'transform-close'; }, 700 );
    
    setTimeout( () => {
        (document.querySelector('.LoaderBalls') as HTMLElement).style.display = 'flex';
    //sending data to the backend
    this.http.post(backend_url+"/item/book/add" ,body.toString(),header)
    .subscribe(
            response => {
                //show success
                (document.querySelector('.LoaderBalls') as HTMLElement).style.display = 'none';   

                setTimeout( () => {
                    //get response
                    let httpResponse = JSON.parse(JSON.stringify(response));
    
                    if(httpResponse.error){
                        this.responseMsg = httpResponse.error;
                        (document.querySelector('.error') as HTMLElement).style.display = 'block';
                        (document.querySelector('.alert-error') as HTMLElement).style.display = 'block';
                        
                    }else{
                        this.responseMsg = httpResponse.success;
                        (document.querySelector('.check_mark') as HTMLElement).style.display = 'block';
                        (document.querySelector('.alert-success') as HTMLElement).style.display = 'block';
                        setTimeout( () => {
    
                            //show available spaces
                            alert("Spaces availble\nBooks: "+httpResponse.Books+"\nDVDs: "+httpResponse.DVDs);
                        },1000);
                    } 
    
                }, 700 );
                
            },
            error => {
                (document.querySelector('.LoaderBalls') as HTMLElement).style.display = 'none';
                (document.querySelector('.error') as HTMLElement).style.display = 'block';
                console.log("Error", error);
            }
        ); 
        }, 700 );    
    //this.activeModal.close(this.addNewBookForm.value);
  }


  ngOnInit() {
      //init form
    this.addNewBookForm = this.formBuilder.group({
        isbn: ['', [Validators.required, Validators.pattern("^[0-9]*$")]],
        title: ['', Validators.required],
        sector: ['', Validators.required],
        authors: this.formBuilder.array([
            this.initAuthors(),
        ]),
        publisher: ['', [Validators.required]],
        publisheDate: ['', [Validators.required]],
        noOfPages: ['', [Validators.required, Validators.pattern("^[0-9]*$")]]
    });
}

initAuthors() :FormGroup {
    return this.formBuilder.group({
        author: ['', [Validators.required]]
    });
}

addAuthor() {
    const control = <FormArray>this.addNewBookForm.controls['authors'];
    control.push(this.initAuthors());
}

removeAuthor(i: number) {
    const control = <FormArray>this.addNewBookForm.controls['authors'];
    control.removeAt(i);
}

get f() { return this.addNewBookForm.controls; }


}
