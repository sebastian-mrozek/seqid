<script lang="ts">
  import type { NumericSequence } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  export let sequence: NumericSequence;

  function onClick(event) {
    dispatch("increment", sequence.id);
  }
</script>

<div class="container">
  <div class="seq-name-ns">
    <span class="seq-name">{sequence.sequenceDefinition.name}</span>
    <span class="seq-ns">{sequence.sequenceDefinition.namespace}</span>
  </div>
  <div class="seq-value">{sequence.lastValue}</div>
  <div class="seq-actions">
    <button class="increment" on:click={onClick}>+</button>
  </div>
</div>

<style>
  .container {
    display: flex;
    font-size: 1.1em;
  }
  .seq-value {
    color: #333;
    padding: 0.2em;
    font-size: 0.8em;
    font-weight: bold;
  }
  .seq-name-ns {
    width: 30%;
  }
  .seq-name {
    font-weight: bold;
    color: #333;
  }
  .seq-ns {
    font-size: 0.7em;
    border-radius: 3px;
    padding: 0.4em;
    background-color: #ddd;
    color: #555;
  }
  .seq-actions {
    margin-left: auto;
  }
</style>
