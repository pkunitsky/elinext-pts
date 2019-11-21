import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import Project from '../models/Project.model';
import PageProject from '../models/PageProject.model';
import User from '../models/User.model';
import PageChangeLog from '../models/PageChangeLog.model';
import PageTask from '../models/PageTask.model';
import Task from '../models/Task.model';
import PageTimeReport from '../models/PageTimeReport.model';
import TimeReport from '../models/TimeReport.model';
import { ROUTE_TYPE } from '../constants';
import { DataType } from '../models/Types';

const BASE_URL = 'http://172.16.1.168:8081';

// ToDo Email and Role should be passed
const EMAIL = 'admin@mail.ru';
const ROLE = 'ADMIN';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({Email: EMAIL, Role: ROLE, 'Content-Type': 'application/json'});
  }

  public getProjects(page: number): Observable<PageProject> {
    const url = `${BASE_URL}/projects`;
    const params = new HttpParams().set('page', page.toString());
    const options = { headers: this.getHeaders(), params };
    return this.http.get<PageProject>(url, options);
  }

  public getProjectById(id: number): Observable<Project> {
    const url = `${BASE_URL}/projects/${id}`;
    const options = { headers: this.getHeaders() };
    return this.http.get<Project>(url, options);
  }

  public createProject(project: Project): Observable<Project> {
    const url = `${BASE_URL}/projects`;
    const options = { headers: this.getHeaders() };
    const body = JSON.stringify(project);
    return this.http.post<Project>(url, body, options);
  }

  public archiveProjects(ids: number[]): Observable<Project> {
    const url = `${BASE_URL}/projects/archive`;
    const options = { headers: this.getHeaders() };
    const body = JSON.stringify({ids});
    return this.http.put<Project>(url, body, options);
  }

  public deleteProject(id: number): Observable<Project> {
    const url = `${BASE_URL}/projects/${id}`;
    const options = { headers: this.getHeaders() };
    return this.http.delete<Project>(url, options);
  }

  public getManagerEmails(): Observable<User[]> {
    const url = `${BASE_URL}/users/manager-emails`;
    const options = { headers: this.getHeaders() };
    return this.http.get<User[]>(url, options);
  }

  public editProject(project: Project): Observable<Project> {
    const url = `${BASE_URL}/projects/${project.id}`;
    const body = JSON.stringify(project);
    const options = { headers: this.getHeaders() };
    return this.http.put<Project>(url, body, options);
  }

  public getChangesLogById(id: number, page: number): Observable<PageChangeLog> {
    const url = `${BASE_URL}/logs/${id}`;
    const params = new HttpParams().set('page', page.toString());
    const options = { headers: this.getHeaders(), params };
    return this.http.get<PageChangeLog>(url, options);
  }

  public getUsers(): Observable<User[]> {
    const url = `${BASE_URL}/users`;
    const options = { headers: this.getHeaders() };
    return this.http.get<User[]>(url, options);
  }

  public getTasksByProjectId(id: number, page: number): Observable<PageTask> {
    const url = `${BASE_URL}/tasks/project/${id}`;
    const params = new HttpParams().set('page', page.toString());
    const options = { headers: this.getHeaders(), params };
    return this.http.get<PageTask>(url, options);
  }

  public createTask(task: Task): Observable<Task> {
    const url = `${BASE_URL}/tasks`;
    const options = { headers: this.getHeaders() };
    const body = JSON.stringify(task);
    return this.http.post<Task>(url, body, options);
  }

  public deleteTask(id: number): Observable<Task> {
    const url = `${BASE_URL}/tasks/${id}`;
    const options = { headers: this.getHeaders() };
    return this.http.delete<Task>(url, options);
  }

  public archiveTasks(ids: number[], projectId: number): Observable<Task> {
    const url = `${BASE_URL}/tasks/archive`;
    const options = { headers: this.getHeaders() };
    const body = JSON.stringify({ids, projectId, name: '*'});
    return this.http.put<Task>(url, body, options);
  }

  public getTaskById(id: number): Observable<Task> {
    const url = `${BASE_URL}/tasks/${id}`;
    const options = { headers: this.getHeaders() };
    return this.http.get<Task>(url, options);
  }

  public editTask(task: Task, id: number): Observable<Task> {
    const url = `${BASE_URL}/tasks/${id}`;
    const body = JSON.stringify(task);
    const options = { headers: this.getHeaders() };
    return this.http.put<Task>(url, body, options);
  }

  public getAllTasks(): Observable<Task[]> {
    const url = `${BASE_URL}/tasks/all`;
    const options = { headers: this.getHeaders() };
    return this.http.get<Task[]>(url, options);
  }

  public moveTasks(ids: number[], projectId: number, parentId: number): Observable<Task> {
    const url = `${BASE_URL}/tasks/move`;
    const body = JSON.stringify({ids, projectId, parentId, name: '*'});
    const options = { headers: this.getHeaders() };
    return this.http.put<Task>(url, body, options);
  }

  public getReportsByTaskId(taskId: number, page: number): Observable<PageTimeReport> {
    const url = `${BASE_URL}/reports/${taskId}`;
    const params = new HttpParams().set('page', page.toString());
    const options = { headers: this.getHeaders(), params };
    return this.http.get<PageTimeReport>(url, options);
  }

  public createReport(report: TimeReport): Observable<TimeReport> {
    const url = `${BASE_URL}/reports`;
    const options = { headers: this.getHeaders() };
    const body = JSON.stringify(report);
    return this.http.post<TimeReport>(url, body, options);
  }

  public editReport(report: TimeReport, id: number): Observable<TimeReport> {
    const url = `${BASE_URL}/reports/${id}`;
    const body = JSON.stringify(report);
    const options = { headers: this.getHeaders() };
    return this.http.put<TimeReport>(url, body, options);
  }

  public deleteReport(id: number): Observable<TimeReport> {
    const url = `${BASE_URL}/reports/${id}`;
    const options = { headers: this.getHeaders() };
    return this.http.delete<TimeReport>(url, options);
  }

  public deleteInstance(id: number, type: DataType): Observable<Project | Task> {
    const url = `${BASE_URL}/${ROUTE_TYPE[type]}/${id}`;
    const options = { headers: this.getHeaders() };
    return this.http.delete<Project | Task>(url, options);
  }

  public archiveInstance(ids: number[], dataType: DataType, projectId?: number): Observable<Project | Task> {
    const url = `${BASE_URL}/${ROUTE_TYPE[dataType]}/archive`;
    const options = { headers: this.getHeaders() };
    const payload = projectId ? {ids, projectId, name: '*'} : {ids};
    const body = JSON.stringify(payload);
    return this.http.put<Project | Task>(url, body, options);
  }

  public createInstance(instance: (Project | Task | TimeReport), type: DataType): Observable<Project | Task | TimeReport> {
    const url = `${BASE_URL}/${ROUTE_TYPE[type]}`;
    const options = { headers: this.getHeaders() };
    const body = JSON.stringify(instance);
    return this.http.post<Project | Task | TimeReport>(url, body, options);
  }
}
