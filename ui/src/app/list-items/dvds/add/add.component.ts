import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormBuilder, FormControl, Validators, FormArray } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { backend_url } from '../../../global';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddDVDComponent implements OnInit {

  addNewDVDForm: FormGroup;
  responseMsg: String;

  constructor(public activeModal: NgbActiveModal,private formBuilder: FormBuilder,private http:HttpClient) { }

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  submitForm() {
        //post request body
        let body = new URLSearchParams();
        body.append('isbn', this.addNewDVDForm.value.isbn);
        body.append('title', this.addNewDVDForm.value.title);
        body.append('sector', this.addNewDVDForm.value.sector);
        body.append('producer', this.addNewDVDForm.value.producer);
        body.append('pubDate', this.addNewDVDForm.value.pubDate);
        body.append('language', this.addNewDVDForm.value.language);
        
        //adding actors
        for(let i=0; i < this.addNewDVDForm.value.actor.length; i++){
            body.append('actors', this.addNewDVDForm.value.actor[i].actor);
        
        }

        for(let i=0; i < this.addNewDVDForm.value.subtitle.length; i++){
            body.append('subtitle', this.addNewDVDForm.value.subtitle[i].subtitle);
        
        }

        //set post sending type
        let header = {headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')};
    
        //hide the form and display loading
    (document.querySelector('form') as HTMLElement).className = 'transform-bounce';
    setTimeout( () => { (document.querySelector('form') as HTMLElement).className = 'transform-close'; }, 700 );
    
    setTimeout( () => {
        (document.querySelector('.LoaderBalls') as HTMLElement).style.display = 'flex';
    //sending data to the backend
    this.http.post(backend_url+"/item/dvd/add" ,body.toString(),header)
    .subscribe(
            response => {
                //hide loader
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
    this.addNewDVDForm = this.formBuilder.group({
        isbn: ['', [Validators.required, Validators.pattern("^[0-9]*$")]],
        title: ['', Validators.required],
        sector: ['', Validators.required],
        producer: ['', [Validators.required]],
        pubDate: ['', [Validators.required]],
        actor: this.formBuilder.array([
            this.initActor(),
        ]),
        language: ['', Validators.required],
        subtitle: this.formBuilder.array([
            this.initSubtitle(),
        ]),
    });
}

initActor() :FormGroup {
    return this.formBuilder.group({
        actor: ['', [Validators.required]]
    });
}

initSubtitle() :FormGroup {
    return this.formBuilder.group({
        subtitle: ['', [Validators.required]]
    });
}

addActor() {
    const control = <FormArray>this.addNewDVDForm.controls['actor'];
    control.push(this.initActor());
}

removeActor(i: number) {
    const control = <FormArray>this.addNewDVDForm.controls['actor'];
    control.removeAt(i);
}

addSubtitle() {
    const control = <FormArray>this.addNewDVDForm.controls['subtitle'];
    control.push(this.initSubtitle());
}

removeSubtitle(i: number) {
    const control = <FormArray>this.addNewDVDForm.controls['subtitle'];
    control.removeAt(i);
}

get f() { return this.addNewDVDForm.controls; }


}
