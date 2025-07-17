# Tasks - To Do list

## Api architecture

1. Tasks are grouped into lists
2. `ListController` has the following endpoints:
   - `GET /lists` - get all lists
   - `GET /lists/{id}` - get specific list by id
   - `GET /lists/{id}/tasks` - get all task summaries for given list
   - `POST /lists` - create a new list
   - `PATCH /lists/{id}` - correct existing list specific params
   - `DELETE /lists/{id}` - delete specified list
3. `TaskController` has the following endpoints:
   - `GET /tasks` - get all tasks summaries like name, date, status (query params for filtering sorting and pagination)
   - `GET /tasks/{id}` - get specified task details
   - `POST /tasks` - create a new task. In request body there should be specified which list(s) to save it to
   - `PATCH /tasks/{id}` - update the task's status (only status)
   - `DELETE /tasks/{id}` - delete given task