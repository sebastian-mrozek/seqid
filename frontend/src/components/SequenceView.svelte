<script lang="ts">
  import type { NumericSequence } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  export let sequence: NumericSequence;

  function onClick(event) {
    dispatch("increment", sequence.id);
  }
</script>

<div>
  <span>{sequence.sequenceDefinition.name}</span>
  <span class="namespace">{sequence.sequenceDefinition.namespace}</span>
  <span class="seq-value">{sequence.lastValue}</span>
  <button class="increment" on:click={onClick}>+</button>
</div>

<style>
  .seq-value {
    color: #333;
    padding: 0.2em;
    font-size: 0.8em;
  }
  .namespace {
    font-size: 0.7em;
    border-radius: 3px;
    padding: 0.4em;
    background-color: #ddd;
    color: #555;
  }
  .increment {
    border-radius: 5px;
  }
</style>
