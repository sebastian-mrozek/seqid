<script lang="ts">
  import type { NumericSequence } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();
  import { blur } from "svelte/transition";

  export let sequence: NumericSequence;
</script>

<tr in:blur>
  <td data-label="Name @ Namespace">
    <span class="seq-name">{sequence.sequenceDefinition.name}</span>
    @
    <span class="seq-ns">{sequence.sequenceDefinition.namespace}</span>
  </td>
  <td data-label="Last Value">{sequence.lastValue ? sequence.lastValue : "not initialized"}</td>
  <td data-label="Actions">
    <button class="rounded" on:click={(e) => dispatch("increment", sequence.id)}>increment</button>
    <button class="rounded" on:click={(e) => dispatch("reset", sequence.id)}>reset</button>
    <button class="rounded" on:click={(e) => dispatch("remove", sequence.id)}>delete</button>
  </td>
</tr>

<div class="container">
  <div class="seq-name-ns" />
  <div class="seq-value" />
  <div class="seq-actions" />
</div>

<style>
</style>
