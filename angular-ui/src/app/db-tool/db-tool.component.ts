import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DbSnapshot} from "../common/common-model";
import {timer} from "rxjs";
import {DbToolService} from "./db-tool.service";

@Component({
  selector: 'app-db-tool',
  templateUrl: './db-tool.component.html',
  styleUrls: ['./db-tool.component.scss']
})
export class DbToolComponent implements OnInit {
  private static MAX_NUM_OF_CHANGED_COPIES = 10;
  private dbSnapshots: Array<DbSnapshot> = new Array<DbSnapshot>(DbToolComponent.MAX_NUM_OF_CHANGED_COPIES);
  private nextPos = 0;

  @Input()
  shouldRefresh: boolean = false;
  @Output()
  refreshed = new EventEmitter<boolean>();

  constructor(private dbToolService: DbToolService) { }

  ngOnInit(): void {
    this.dbToolService.dbSnapshot().subscribe(snapshot => this.addSnapshotIfChanged(snapshot))
    this.refreshed.emit(true);
    // timer(1000, 1000).subscribe(v => {
    //     this.dbToolService.dbSnapshot().subscribe(snapshot => this.addSnapshotIfChanged(snapshot))
    // })
  }

  refresh(): void {
    this.dbToolService.dbSnapshot().subscribe(snapshot => this.addSnapshotIfChanged(snapshot))
    this.refreshed.emit(true);
  }

  private addSnapshotIfChanged(snapshot: DbSnapshot): void {
    let prev = (this.nextPos + DbToolComponent.MAX_NUM_OF_CHANGED_COPIES - 1) % DbToolComponent.MAX_NUM_OF_CHANGED_COPIES;
    if (snapshot.isChanged(this.dbSnapshots[prev])) {
      snapshot.atTime = new Date();
      this.dbSnapshots[this.nextPos ++] = snapshot;
    } else {
      this.dbSnapshots[prev].atTime = new Date();
    }
  }

  isEmpty(): boolean {
    return this.nextPos == 0 && !this.isCircledBack();
  }

  descendingSnapshots(): Array<DbSnapshot> {
    if (this.isEmpty()) {
      return new Array<DbSnapshot>();
    } else if (this.isCircledBack()) {
      let ret = this.dbSnapshots.filter( (v, i, a) => i >= this.nextPos)
      ret.push( ...this.dbSnapshots.filter( (v, i, a) => i < this.nextPos) )
      ret.reverse();
      return ret;
    } else {
      return this.dbSnapshots.filter( a => a !== undefined).reverse();
    }
  }

  private isCircledBack(): boolean {
    return this.dbSnapshots[DbToolComponent.MAX_NUM_OF_CHANGED_COPIES - 1] !== undefined;
  }
}
