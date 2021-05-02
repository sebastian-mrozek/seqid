import axios from "axios";
import type { NumericSequence, NumericSequenceDefinition } from "./model";

interface SequenceService {
  getAll(onResolve: (data: NumericSequence[]) => void): void;
  create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void): void;
  increment(id: string, onSuccess: (nextValue: number) => void): void;
}

export const sequenceService: SequenceService = ((): SequenceService => {
  const restApiClient = axios.create({ baseURL: "http://localhost:7000/sequence" });

  function handleError(error) {
    console.log(error);
  }
  function log(response) {
    console.log(response);
  }

  function getAll(onSuccess: (data: NumericSequence[]) => void): void {
    restApiClient
      .get<NumericSequence[]>("")
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(handleError);
  }

  function create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void): void {
    restApiClient
      .post<NumericSequence>("", newSequenceDefinition)
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(handleError);
  }
  function increment(id: string, onSuccess: (nextValue: number) => void): void {
    restApiClient
      .get<number>(id + "/next")
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(handleError);
  }
  return {
    getAll,
    create,
    increment,
  };
})();
