import axios from "axios";
import type { NumericSequence, NumericSequenceDefinition } from "./model";
import { notifications } from "./stores/app-state";

interface SequenceService {
  getAll(onResolve: (data: NumericSequence[]) => void): void;
  create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void): void;
  increment(id: string, onSuccess: (nextValue: number) => void): void;
  reset(id: string, onSuccess: (updatedSequence: NumericSequence) => void): void;
  remove(id: string, onSuccess: () => void): void;
}

export const sequenceService: SequenceService = ((): SequenceService => {
//   const restApiClient = axios.create({ baseURL: "http://localhost:7000/sequence" });
  const restApiClient = axios.create({ baseURL: "sequence" });

  type Action = "FETCH" | "CREATE" | "RESET" | "REMOVE" | "INCREMENT";

  function createErrorHandler(action: Action, text?: string) {
    return (error) => {
      if (error.status === 400 && action === "CREATE") {
        notifications.addError("Sequence with that name and namespace already exists");
      } else {
        notifications.addError(error.message);
      }
      console.log(error);
    };
  }

  function log(response) {
    console.log(response);
  }

  async function getAll(onSuccess: (data: NumericSequence[]) => void) {
    restApiClient
      .get<NumericSequence[]>("")
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(createErrorHandler("FETCH"));
  }

  async function create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void) {
    restApiClient
      .post<NumericSequence>("", newSequenceDefinition)
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(createErrorHandler("CREATE"));
  }
  async function increment(id: string, onSuccess: (nextValue: number) => void) {
    restApiClient
      .get<number>(id + "/next")
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(createErrorHandler("INCREMENT"));
  }
  async function reset(id: string, onSuccess: (updatedSequence: NumericSequence) => void) {
    restApiClient
      .patch<NumericSequence>(id)
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(createErrorHandler("RESET"));
  }

  async function remove(id: string, onSuccess: () => void) {
    restApiClient
      .delete(id)
      .then((response) => {
        log(response);
        onSuccess();
      })
      .catch(createErrorHandler("REMOVE"));
  }
  return {
    getAll,
    create,
    increment,
    reset,
    remove,
  };
})();
