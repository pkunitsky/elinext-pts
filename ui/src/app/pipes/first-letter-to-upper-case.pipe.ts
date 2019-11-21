import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'firstLetterToUpperCase'
})
export class FirstLetterToUpperCasePipe implements PipeTransform {

  transform(value: string): any {
    return value[0].toUpperCase() + value.slice(1).toLowerCase();
  }
}
