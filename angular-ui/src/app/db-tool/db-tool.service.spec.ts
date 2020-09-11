import { TestBed } from '@angular/core/testing';

import { DbToolService } from './db-tool.service';

describe('DbToolService', () => {
  let service: DbToolService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DbToolService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
