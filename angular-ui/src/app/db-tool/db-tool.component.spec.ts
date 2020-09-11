import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DbToolComponent } from './db-tool.component';

describe('DbToolComponent', () => {
  let component: DbToolComponent;
  let fixture: ComponentFixture<DbToolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DbToolComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DbToolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
