import axios from "axios";
import type { NumericSequence, NumericSequenceDefinition } from "./model";

interface SequenceService {
  getAll(onResolve: (data: NumericSequence[]) => void): void;
  create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void): void;
  increment(id: string, onSuccess: (nextValue: number) => void): void;
  reset(id: string, onSuccess: (updatedSequence: NumericSequence) => void): void;
  remove(id: string, onSuccess: () => void): void;
}

export const sequenceService: SequenceService = ((): SequenceService => {
  const restApiClient = axios.create({ baseURL: "http://localhost:7000/sequence" });

  function handleError(error) {
    console.log(error);
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
      .catch(handleError);
  }

  async function create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void) {
    restApiClient
      .post<NumericSequence>("", newSequenceDefinition)
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(handleError);
  }
  async function increment(id: string, onSuccess: (nextValue: number) => void) {
    restApiClient
      .get<number>(id + "/next")
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(handleError);
  }
  async function reset(id: string, onSuccess: (updatedSequence: NumericSequence) => void) {
    restApiClient
      .patch<NumericSequence>(id)
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(handleError);
  }

  async function remove(id: string, onSuccess: () => void) {
    restApiClient
      .delete(id)
      .then((response) => {
        log(response);
        onSuccess();
      })
      .catch(handleError);
  }
  return {
    getAll,
    create,
    increment,
    reset,
    remove,
  };
})();
