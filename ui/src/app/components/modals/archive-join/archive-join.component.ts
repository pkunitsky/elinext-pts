import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import DialogData from '../../../models/DialogData.model';

const OPTION_LENGTH = 45;

@Component({
  selector: 'pts-dialog',
  templateUrl: './archive-join.component.html',
  styleUrls: ['./archive-join.component.scss']
})
export class ArchiveJoinComponent implements OnInit {
  public options: object[];
  public value: any;

  constructor(
    public dialogRef: MatDialogRef<ArchiveJoinComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
  }

  ngOnInit(): void {
    // ToDo rewrite method when data will be received from server
    if (this.data.projects) {
      this.options = this.data.projects.map(({name, id}) => {
        if (name.length > OPTION_LENGTH) {
          return {option: name.substring(0, OPTION_LENGTH).trim() + '...', id};
        } else {
          return {option: name, id};
        }
      });
    }
  }
}
