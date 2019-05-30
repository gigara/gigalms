import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { MatTableModule, MatSortModule} from '@angular/material';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import 'hammerjs';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { MenuComponent } from './menu/menu.component';
import { RouterModule, Routes} from '@angular/router';
import { MaterialModule} from './material.module';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormComponent } from './borrow/form/form.component';
import { ReturnFormComponent} from './return/form/form.component';
import { ListItemsComponent } from './list-items/list-items.component';
import { BooksComponent } from './list-items/books/books.component';
import { DVDsComponent } from './list-items/dvds/dvds.component';
import { RegisterComponent } from './user/register/register.component';
import { AddBookComponent } from './list-items/books/add/add.component';
import { AddDVDComponent } from './list-items/dvds/add/add.component';
import { ReportComponent } from './report/report/report.component';
import { DeleteComponent } from './delete/delete.component';
import { ReserveComponent } from './reserve/reserve.component';
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MenuComponent,
    FormComponent,
    ListItemsComponent,
    BooksComponent,
    DVDsComponent,
    RegisterComponent,
    ReturnFormComponent,
    AddBookComponent,
    AddDVDComponent,
    ReportComponent,
    DeleteComponent,
    ReserveComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    NgbModule,
    RouterModule.forRoot([
      {path: '', component: MenuComponent},
      {path: 'borrow', component: FormComponent},
      {path: 'return', component: ReturnFormComponent},
      {path: 'list', component: ListItemsComponent},
      {path: 'list/books', component: BooksComponent},
      {path: 'list/dvds', component: DVDsComponent},
      {path: 'user/register', component: RegisterComponent},
      {path: 'reserve', component: ReserveComponent},
      {path: 'report', component: ReportComponent},
      {path: 'delete', component: DeleteComponent},
      {path: 'search', component: SearchComponent}
    ])
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [
    AddBookComponent,
    AddDVDComponent
  ]
})
export class AppModule { }
