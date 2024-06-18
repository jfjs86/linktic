import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'boolean'
})
export class BooleanPipe implements PipeTransform {

  transform(value: boolean, trueText: string = 'Si', falseText: string = 'No'): string {
    return value ? trueText : falseText;
  }
}
