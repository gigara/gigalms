import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DVDsComponent } from './dvds.component';

describe('DvdsComponent', () => {
  let component: DVDsComponent;
  let fixture: ComponentFixture<DVDsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DVDsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DVDsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
