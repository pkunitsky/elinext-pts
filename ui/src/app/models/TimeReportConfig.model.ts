import { SortByType, SortOrderType } from './Types';

export default interface TimeReportConfig {
  filters?: object;
  id?: number;
  name?: string;
  numberOfFilteredRecords?: number;
  sortBy?: SortByType;
  sortByTypes?: SortByType[];
  sortOrder?: SortOrderType;
  sortOrderTypes?: SortOrderType[];
}
